package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.TestUtils;
import team.moca.camo.api.KakaoAuthApiService;
import team.moca.camo.domain.User;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@Transactional
@ExtendWith(value = MockitoExtension.class)
public class KakaoAuthServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private KakaoAuthApiService kakaoAuthApiService;

    @DisplayName("기존 계정과 카카오 계정 연동에 성공한다.")
    @Test
    void integrateKakaoAccountWithEmailAccountSuccess() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        String testKakaoId = "Test Kakao ID";
        when(kakaoAuthApiService.getKakaoAccountId(anyString())).thenReturn(testKakaoId);

        // when
        authenticationService.integrateKakaoAccountWithEmailAccount(testUser.getId(), "test");

        // then
        assertThat(testUser.getKakaoId()).isNotNull();
        assertThat(testUser.getKakaoId()).isEqualTo(testKakaoId);
    }

    @DisplayName("인증되지 않은 사용자는 카카오 계정 연동에 실패한다.")
    @Test
    void integrateKakaoAccountWithEmailAccountFailUnauthenticatedUser() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        when(userRepository.findById(testUser.getId()))
                .thenThrow(new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));
        String testKakaoId = "Test Kakao ID";
        when(kakaoAuthApiService.getKakaoAccountId(anyString())).thenReturn(testKakaoId);

        // when
        assertThatThrownBy(() ->
                authenticationService.integrateKakaoAccountWithEmailAccount(testUser.getId(), "test"));

        // then
    }

    @DisplayName("카카오 인증 토큰이 유효하지 않은 경우 계정 연동에 실패한다.")
    @Test
    void integrateKakaoAccountWithEmailAccountFailInvalidKakaoToken() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        String testKakaoId = "Test Kakao ID";
        when(kakaoAuthApiService.getKakaoAccountId(anyString()))
                .thenThrow(new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));

        // when
        assertThatThrownBy(() ->
                authenticationService.integrateKakaoAccountWithEmailAccount(testUser.getId(), "test"));

        // then
    }
}
