package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.TestUtils;
import team.moca.camo.domain.User;
import team.moca.camo.repository.UserRepository;
import team.moca.camo.security.jwt.JwtUtils;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@DisplayName("인증 테스트")
@Transactional
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
        User testUser = TestUtils.createTestUser();
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
        User testUser = TestUtils.createTestUser();
        userRepository.save(testUser);

        // when
        String refreshToken = jwtUtils.generateToken(testUser, Duration.ofMillis(1));
        log.info("refreshToken = {}", refreshToken);
        Thread.sleep(10);

        // then
        assertThatThrownBy(() -> authenticationService.getNewAccessToken(refreshToken));
    }
}