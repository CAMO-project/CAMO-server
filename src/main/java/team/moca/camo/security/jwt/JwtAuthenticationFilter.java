package team.moca.camo.security.jwt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(requestTokenHeader) || !isValidateRequestTokenHeader(requestTokenHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestToken = requestTokenHeader.substring(TOKEN_PREFIX.length());

        if (jwtUtils.isValidToken(requestToken)) {
            Authentication authentication = jwtUtils.getAuthentication(requestToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidateRequestTokenHeader(String requestTokenHeader) {
        if (!StringUtils.startsWith(requestTokenHeader, TOKEN_PREFIX)) {
            logger.error("Authorization request header does not begin with 'Bearer ' String!");
            return false;
        }
        return true;
    }
}
