package team.moca.camo.security.mock;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MockFilterChain implements FilterChain {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) {
    }
}
