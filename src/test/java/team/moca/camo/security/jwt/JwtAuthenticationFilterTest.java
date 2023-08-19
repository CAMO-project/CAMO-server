package team.moca.camo.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team.moca.camo.TestInstanceFactory;
import team.moca.camo.domain.User;
import team.moca.camo.security.mock.MockFilterChain;
import team.moca.camo.security.mock.MockHttpServletRequest;
import team.moca.camo.security.mock.MockJwtProperties;
import team.moca.camo.security.mock.MockUserDetailsService;

import java.time.Duration;

@Slf4j
@DisplayName("JwtAuthetnicationFilter 테스트")
public class JwtAuthenticationFilterTest {

    private final JwtUtils jwtUtils = new JwtUtils(new MockJwtProperties(), new MockUserDetailsService());
    private final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);

    @DisplayName("유효한 JWT의 경우 필터를 통과한다.")
    @Test
    void passFilterValidJWT() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        String token = jwtUtils.generateToken(testUser, Duration.ofMinutes(1));

        // when
        MockHttpServletRequest testRequest = new MockHttpServletRequest(token);

        // then
        jwtAuthenticationFilter.doFilterInternal(testRequest, null, new MockFilterChain());
    }
}
