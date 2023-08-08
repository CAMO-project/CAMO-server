package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.moca.camo.TestUtils;
import team.moca.camo.api.KakaoAuthApiService;
import team.moca.camo.controller.dto.response.LoginResponse;
import team.moca.camo.domain.User;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.repository.UserRepository;
import team.moca.camo.security.jwt.JwtUtils;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@DisplayName("카카오 인증 테스트")
@ExtendWith(value = MockitoExtension.class)
public class KakaoAuthServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private KakaoAuthApiService kakaoAuthApiService;
    @Mock
    private JwtUtils jwtUtils;

    @DisplayName("기존 계정과 카카오 계정 연동에 성공한다.")
    @Test
    void integrateKakaoAccountWithEmailAccountSuccess() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        // when
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        String testKakaoId = "Test Kakao ID";
        when(kakaoAuthApiService.getKakaoAccountId(anyString())).thenReturn(testKakaoId);

        // then
        authenticationService.integrateKakaoAccountWithEmailAccount(testUser.getId(), "test");

        assertThat(testUser.getKakaoId()).isNotNull();
        assertThat(testUser.getKakaoId()).isEqualTo(testKakaoId);
    }

    @DisplayName("인증되지 않은 사용자는 카카오 계정 연동에 실패한다.")
    @Test
    void integrateKakaoAccountWithEmailAccountFailUnauthenticatedUser() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        // when
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() ->
                authenticationService.integrateKakaoAccountWithEmailAccount(testUser.getId(), "test"));
    }

    @DisplayName("카카오 인증 토큰이 유효하지 않은 경우 계정 연동에 실패한다.")
    @Test
    void integrateKakaoAccountWithEmailAccountFailInvalidKakaoToken() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        // when
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(kakaoAuthApiService.getKakaoAccountId(anyString()))
                .thenThrow(new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));

        // then
        assertThatThrownBy(() ->
                authenticationService.integrateKakaoAccountWithEmailAccount(testUser.getId(), "test"));
    }

    @DisplayName("카카오 계정이 연동되어 있는 경우 카카오 로그인에 성공한다.")
    @Test
    void loginWithKakaoAccountSuccess() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        // when
        when(kakaoAuthApiService.getKakaoAccountId(anyString())).thenReturn(testUser.getKakaoId());
        when(userRepository.findByKakaoId(testUser.getKakaoId())).thenReturn(Optional.of(testUser));
        when(jwtUtils.generateToken(eq(testUser), any(Duration.class))).thenReturn("json web token");

        // then
        LoginResponse loginResponse = authenticationService.loginWithKakaoAccount("test");

        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getAccessToken()).isNotNull();
        assertThat(loginResponse.getRefreshToken()).isNotNull();
    }

    @DisplayName("카카오 계정이 연동되어 있지 않은 경우 카카오 로그인에 실패한다.")
    @Test
    void loginWithKakaoAccountFailKakaoAccountDoNotIntegrated() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        // when
        when(kakaoAuthApiService.getKakaoAccountId(anyString())).thenReturn(testUser.getKakaoId());
        when(userRepository.findByKakaoId(testUser.getKakaoId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> authenticationService.loginWithKakaoAccount("test"));
    }

    @DisplayName("카카오 인증 토큰이 유효하지 않은 경우 카카오 로그인에 실패한다.")
    @Test
    void loginWithKakaoAccountFailInvalidKakaoToken() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        // when
        when(kakaoAuthApiService.getKakaoAccountId(anyString()))
                .thenThrow(new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));

        // then
        assertThatThrownBy(() -> authenticationService.loginWithKakaoAccount("test"));
    }
}
