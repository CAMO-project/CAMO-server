package team.moca.camo.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import team.moca.camo.common.ThreadSafeDistinctMemory;
import team.moca.camo.domain.User;
import team.moca.camo.exception.CamoException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.repository.UserRepository;
import team.moca.camo.security.jwt.JwtUtils;
import team.moca.camo.service.sms.SmsService;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthenticationService {

    private static final ThreadSafeDistinctMemory<String> threadSafeDistinctMemory =
            new ThreadSafeDistinctMemory<>();
    private static final Map<String, Integer> phoneVerificationCodeMemory = new ConcurrentHashMap<>();
    private static final Duration ACCESS_TOKEN_VALIDITY_PERIOD = Duration.ofHours(2);
    private static final Duration REFRESH_TOKEN_VALIDITY_PERIOD = Duration.ofDays(30);

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final SmsService smsService;

    public AuthenticationService(JwtUtils jwtUtils, UserRepository userRepository, SmsService smsService) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.smsService = smsService;
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

    public void sendVerificationCodeMessage(String phone) {
        int verificationCode = new Random().nextInt(1000000);

        StringBuilder contents = new StringBuilder();
        contents.append("CAMO 인증번호 [").append(verificationCode).append("]");

        smsService.sendSimpleMessage(phone, contents);
        phoneVerificationCodeMemory.put(phone, verificationCode);
    }

    public String checkEmailDuplicate(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new CamoException(AuthenticationError.EMAIL_DUPLICATE);
        }
        if (threadSafeDistinctMemory.contains(email)) {
            throw new CamoException(AuthenticationError.EMAIL_DUPLICATE);
        }

        return email;
    }
}
