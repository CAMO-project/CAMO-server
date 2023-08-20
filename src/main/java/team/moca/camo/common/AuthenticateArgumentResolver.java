package team.moca.camo.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import team.moca.camo.common.annotation.Authenticate;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.security.jwt.JwtUtils;

import javax.servlet.http.HttpServletRequest;

public class AuthenticateArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtils jwtUtils;

    public AuthenticateArgumentResolver(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticate.class) &&
                String.class.equals(parameter.getParameterType());
    }

    @Override
    public String resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = getTokenFromRequestHeader(request);
        if (!jwtUtils.isValidToken(token)) {
            checkRequired(parameter);
            return null;
        }
        return jwtUtils.extractAccountIdFromToken(token);
    }

    private String getTokenFromRequestHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authorizationHeader)) {
            return null;
        }

        return authorizationHeader.split(" ")[1];
    }

    private void checkRequired(MethodParameter parameter) {
        Authenticate authenticateAnnotation = parameter.getParameterAnnotation(Authenticate.class);
        if (authenticateAnnotation.required()) {
            throw new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL);
        }
    }
}
