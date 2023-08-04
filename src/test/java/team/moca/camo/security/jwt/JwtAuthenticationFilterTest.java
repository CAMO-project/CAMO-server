package team.moca.camo.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.moca.camo.TestUtils;
import team.moca.camo.domain.User;
import team.moca.camo.security.mock.MockFilterChain;
import team.moca.camo.security.mock.MockHttpServletRequest;
import team.moca.camo.security.mock.MockJwtProperties;
import team.moca.camo.security.mock.MockUserDetailsService;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@DisplayName("JwtAuthetnicationFilter 테스트")
public class JwtAuthenticationFilterTest {

    private final JwtUtils jwtUtils = new JwtUtils(new MockJwtProperties(), new MockUserDetailsService());
    private final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);

    @DisplayName("유효한 JWT의 경우 필터를 통과한다.")
    @Test
    void passFilterValidJWT() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();
        String token = jwtUtils.generateToken(testUser, Duration.ofMinutes(1));

        // when
        MockHttpServletRequest testRequest = new MockHttpServletRequest(token);

        // then
        jwtAuthenticationFilter.doFilterInternal(testRequest, null, new MockFilterChain());
    }

    @DisplayName("만료된 JWT의 경우 필터에서 예외가 발생한다.")
    @Test
    void filterThrowsExceptionExpiredJWT() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();
        String token = jwtUtils.generateToken(testUser, Duration.ofMillis(10));
        Thread.sleep(100);

        // when
        MockHttpServletRequest testRequest = new MockHttpServletRequest(token);

        // then
        assertThatThrownBy(() ->
                jwtAuthenticationFilter.doFilterInternal(testRequest, null, new MockFilterChain()));
    }

    @DisplayName("변형된 JWT의 경우 필터에서 예외가 발생한다.")
    @Test
    void filterThrowsExceptionModifiedJWT() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();
        String token = jwtUtils.generateToken(testUser, Duration.ofMinutes(1));
        token += "m";

        // when
        MockHttpServletRequest testRequest = new MockHttpServletRequest(token);

        // then
        assertThatThrownBy(() ->
                jwtAuthenticationFilter.doFilterInternal(testRequest, null, new MockFilterChain()));
    }
}