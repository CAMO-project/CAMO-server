package team.moca.camo.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import team.moca.camo.domain.User;
import team.moca.camo.exception.CamoException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.security.jwt.JwtUtils;

import java.time.Duration;

@Service
public class AuthenticationService {

    private static final Duration ACCESS_TOKEN_VALIDITY_PERIOD = Duration.ofHours(2);
    private static final Duration REFRESH_TOKEN_VALIDITY_PERIOD = Duration.ofDays(30);

    private final JwtUtils jwtUtils;

    public AuthenticationService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public String getNewAccessToken(String refreshToken) {
        refreshTokenValidation(refreshToken);

        Authentication authentication = jwtUtils.getAuthentication(refreshToken);
        User authenticatedUser = (User) authentication.getPrincipal();
        return jwtUtils.generateToken(authenticatedUser, ACCESS_TOKEN_VALIDITY_PERIOD);
    }

    private void refreshTokenValidation(String refreshToken) {
        if (jwtUtils.isValidToken(refreshToken)) {
            return;
        }
        throw new CamoException(AuthenticationError.INVALID_TOKEN_ERROR);
    }
}
