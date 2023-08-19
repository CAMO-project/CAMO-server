package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.moca.camo.TestInstanceFactory;
import team.moca.camo.controller.dto.request.LoginRequest;
import team.moca.camo.controller.dto.request.SignUpRequest;
import team.moca.camo.controller.dto.response.LoginResponse;
import team.moca.camo.domain.User;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.repository.UserRepository;
import team.moca.camo.security.jwt.JwtUtils;

import javax.transaction.Transactional;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
@DisplayName("인증 테스트")
@Transactional
@ExtendWith(value = MockitoExtension.class)
@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("Refresh Token이 유효한 경우 새로운 Access Token을 발급한다.")
    @Test
    void issueNewAccessTokenValidRefreshToken() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        userRepository.save(testUser);

        // when
        String refreshToken = jwtUtils.generateToken(testUser, Duration.ofMinutes(1));
        log.info("refreshToken = {}", refreshToken);

        // then
        String accessToken = authenticationService.getNewAccessToken(refreshToken);
        log.info("accessToken = {}", accessToken);

        assertThat(accessToken).isNotBlank();
    }

    @DisplayName("Refresh Token이 만료된 경우 새로운 Access Token 발급에 실패한다.")
    @Test
    void failIssueNewAccessTokenExpiredRefreshToken() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        userRepository.save(testUser);

        // when
        String refreshToken = jwtUtils.generateToken(testUser, Duration.ofMillis(1));
        log.info("refreshToken = {}", refreshToken);
        Thread.sleep(10);

        // then
        assertThatThrownBy(() -> authenticationService.getNewAccessToken(refreshToken));
    }

    @DisplayName("새로운 이메일 계정을 생성(가입)할 수 있다.")
    @Test
    void createNewEmailAccount() throws Exception {
        // given
        SignUpRequest testSignUpRequest = SignUpRequest.builder()
                .email("test@gmail.com")
                .password("test1234")
                .passwordCheck("test1234")
                .phone("01011112222")
                .nickname("nickname")
                .build();

        // when
        String accountId = authenticationService.createNewEmailAccount(testSignUpRequest);
        log.info("accountId = {}", accountId);

        // then
        assertAll(
                () -> assertThat(accountId).isNotBlank(),
                () -> assertThat(accountId).startsWith("user")
        );
    }

    @DisplayName("중복된 이메일의 경우 회원가입에 실패한다.")
    @Test
    void failCreateNewEmailAccountWhenEmailDuplicate() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        userRepository.save(testUser);

        // when
        SignUpRequest testSignUpRequest = SignUpRequest.builder()
                .email("test@gmail.com")
                .password("test1234")
                .passwordCheck("test1234")
                .phone("01011112222")
                .nickname("nickname")
                .build();

        // then
        assertThatThrownBy(() -> authenticationService.createNewEmailAccount(testSignUpRequest));
    }

    @DisplayName("중복된 전화번호인 경우 회원가입에 실패한다.")
    @Test
    void failCreateNewEmailAccountWhenPhoneNumberDuplicate() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        userRepository.save(testUser);

        // when
        SignUpRequest testSignUpRequest = SignUpRequest.builder()
                .email("test1@gmail.com")
                .password("test1234")
                .passwordCheck("test!1234")
                .phone("01012345678")
                .nickname("nickname")
                .build();

        // then
        assertThatThrownBy(() -> authenticationService.createNewEmailAccount(testSignUpRequest));
    }

    @DisplayName("중복된 닉네임인 경우 회원가입에 실패한다.")
    @Test
    void failCreateNewEmailAccountWhenNicknameDuplicate() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        userRepository.save(testUser);

        // when
        SignUpRequest testSignUpRequest = SignUpRequest.builder()
                .email("test1@gmail.com")
                .password("test1234")
                .passwordCheck("test!1234")
                .phone("01011112222")
                .nickname("test")
                .build();

        // then
        assertThatThrownBy(() -> authenticationService.createNewEmailAccount(testSignUpRequest));
    }

    @DisplayName("비밀번호 확인이 일치하지 않는 경우 회원가입에 실패한다.")
    @Test
    void failCreateNewEmailAccountWhenPasswordMismatch() throws Exception {
        // given
        SignUpRequest testSignUpRequest = SignUpRequest.builder()
                .email("test1@gmail.com")
                .password("test1234")
                .passwordCheck("test!1234")
                .phone("01011112222")
                .nickname("nickname")
                .build();

        // when

        // then
        assertThatThrownBy(() -> authenticationService.createNewEmailAccount(testSignUpRequest));
    }

    @DisplayName("올바른 이메일과 비밀번호를 입력하면 로그인이 성공하고 access token과 refresh token이 발급된다.")
    @Test
    void loginSuccessAccessTokenAndRefreshTokenIssued() throws Exception {
        // given
        User user = TestInstanceFactory.getTestUserInstance();
        userRepository.save(user);

        // when
        String email = "test@gmail.com";
        String password = "test1234";
        LoginResponse loginResponse =
                authenticationService.loginWithEmailAccount(new LoginRequest(email, password));
        String accessToken = loginResponse.getAccessToken();
        String refreshToken = loginResponse.getRefreshToken();

        // then
        assertThat(accessToken).isNotBlank();
        assertThat(refreshToken).isNotBlank();
        assertDoesNotThrow(() -> jwtUtils.isValidToken(accessToken));
        assertDoesNotThrow(() -> jwtUtils.isValidToken(refreshToken));
    }

    @DisplayName("존재하지 않는 이메일을 입력하면 로그인에 실패한다.")
    @Test
    void loginFailNonExistEmail() throws Exception {
        // given
        User user = TestInstanceFactory.getTestUserInstance();
        userRepository.save(user);

        // when
        String email = "fali@gmail.com";
        String password = "test1234";

        // then
        assertThatThrownBy(() ->
                authenticationService.loginWithEmailAccount(new LoginRequest(email, password)))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("비밀번호를 올바르게 입력하지 않으면 로그인에 실패한다.")
    @Test
    void loginFailInvalidPassword() throws Exception {
        // given
        User user = TestInstanceFactory.getTestUserInstance();
        userRepository.save(user);

        // when
        String email = "fali@gmail.com";
        String password = "fail1234";

        // then
        assertThatThrownBy(() ->
                authenticationService.loginWithEmailAccount(new LoginRequest(email, password)))
                .isInstanceOf(BusinessException.class);
    }
}