package team.moca.camo.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.moca.camo.TestUtils;
import team.moca.camo.domain.User;
import team.moca.camo.security.mock.MockJwtProperties;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JWT 테스트")
@Slf4j
class JwtUtilsTest {

    private final JwtUtils jwtUtils = new JwtUtils(new MockJwtProperties());

    @DisplayName("JWT를 생성할 수 있다.")
    @Test
    void generateJWT() throws Exception {
        // given
        User testUser = TestUtils.createTestUser();
        log.info("testUser.id = {}", testUser.getId());

        // when
        String token = jwtUtils.generateToken(testUser, Duration.ofMinutes(1));
        log.info("token = {}", token);

        // then
        assertThat(token).isNotNull();
        assertThat(token.split("\\.").length).isEqualTo(3);
    }

    @DisplayName("정상적인 JWT는 유효성 검증에 성공한다.")
    @Test
    void normalJWTSuccessValidation() throws Exception {
        // given
        User testUser = TestUtils.createTestUser();
        String token = jwtUtils.generateToken(testUser, Duration.ofMinutes(1));

        // when
        boolean isValidToken = jwtUtils.isValidToken(token);

        // then
        assertThat(isValidToken).isTrue();
    }

    @DisplayName("JWT가 만료된 경우 유효성 검증에 실패한다.")
    @Test
    void expiredJWTFailValidation() throws Exception {
        // given
        User testUser = TestUtils.createTestUser();
        String token = jwtUtils.generateToken(testUser, Duration.ofMillis(10));
        Thread.sleep(100);

        // when
        boolean isValidToken = jwtUtils.isValidToken(token);

        // then
        assertThat(isValidToken).isFalse();
    }

    @DisplayName("JWT가 변형된 경우 유효성 검증에 실패한다.")
    @Test
    void modifiedJWTFailValidation() throws Exception {
        // given
        User testUser = TestUtils.createTestUser();
        String token = jwtUtils.generateToken(testUser, Duration.ofMinutes(1));
        token += "m";

        // when
        boolean isValidToken = jwtUtils.isValidToken(token);

        // then
        assertThat(isValidToken).isFalse();
    }
}