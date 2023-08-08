package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.api.KakaoAuthApiService;
import team.moca.camo.controller.dto.request.LoginRequest;
import team.moca.camo.controller.dto.request.SignUpRequest;
import team.moca.camo.controller.dto.response.LoginResponse;
import team.moca.camo.domain.User;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.repository.UserRepository;
import team.moca.camo.security.jwt.JwtUtils;
import team.moca.camo.service.sms.SmsService;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Transactional(readOnly = true)
@Service
public class AuthenticationService {

    private static final Map<String, Integer> phoneVerificationCodeMemory = new ConcurrentHashMap<>();
    private static final Duration ACCESS_TOKEN_VALIDITY_PERIOD = Duration.ofHours(2);
    private static final Duration REFRESH_TOKEN_VALIDITY_PERIOD = Duration.ofDays(30);

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder;
    private final KakaoAuthApiService kakaoAuthApiService;

    public AuthenticationService(JwtUtils jwtUtils, UserRepository userRepository, SmsService smsService, PasswordEncoder passwordEncoder, KakaoAuthApiService kakaoAuthApiService) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.smsService = smsService;
        this.passwordEncoder = passwordEncoder;
        this.kakaoAuthApiService = kakaoAuthApiService;
    }

    public String getNewAccessToken(final String refreshToken) {
        refreshTokenValidation(refreshToken);

        Authentication authentication = jwtUtils.getAuthentication(refreshToken);
        User authenticatedUser = (User) authentication.getPrincipal();
        return jwtUtils.generateToken(authenticatedUser, ACCESS_TOKEN_VALIDITY_PERIOD);
    }

    private void refreshTokenValidation(final String refreshToken) {
        if (jwtUtils.isValidToken(refreshToken)) {
            return;
        }
        throw new BusinessException(AuthenticationError.INVALID_TOKEN);
    }

    public void sendVerificationCodeMessage(final String phone) {
        int verificationCode = new Random().nextInt(1000000);

        StringBuilder contents = new StringBuilder();
        contents.append("CAMO 인증번호 [").append(verificationCode).append("]");

        smsService.sendSimpleMessage(phone, contents);
        phoneVerificationCodeMemory.put(phone, verificationCode);
    }

    public String validateEmail(final String email) {
        checkEmailDuplicate(email);
        return email;
    }

    @Transactional
    public String createNewEmailAccount(final SignUpRequest signUpRequest) {
        checkEmailDuplicate(signUpRequest.getEmail());
        checkPhoneDuplicate(signUpRequest.getPhone());
        checkNicknameDuplicate(signUpRequest.getNickname());
        String encodedPassword =
                checkPasswordAndEncode(signUpRequest.getPassword(), signUpRequest.getPasswordCheck());

        User signUpUser = User.signUp(signUpRequest, encodedPassword);
        User savedUser = userRepository.save(signUpUser);
        return savedUser.getId();
    }

    private void checkEmailDuplicate(final String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new BusinessException(AuthenticationError.EMAIL_DUPLICATION);
        }
    }

    private void checkPhoneDuplicate(final String phone) {
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if (optionalUser.isPresent()) {
            throw new BusinessException(AuthenticationError.PHONE_DUPLICATION);
        }
    }

    private void checkNicknameDuplicate(final String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);
        if (optionalUser.isPresent()) {
            throw new BusinessException(AuthenticationError.NICKNAME_DUPLICATION);
        }
    }

    private String checkPasswordAndEncode(final String password, final String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new BusinessException(AuthenticationError.PASSWORD_CHECK_MISMATCH);
        }

        return passwordEncoder.encode(password);
    }

    @Transactional
    public void integrateKakaoAccountWithEmailAccount(final String authenticatedAccountId, final String kakaoToken) {
        User authenticatedUser = userRepository.findById(authenticatedAccountId)
                .orElseThrow(() -> new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));

        String kakaoAccountId = kakaoAuthApiService.getKakaoAccountId(kakaoToken);
        authenticatedUser.integrateKakaoAccount(kakaoAccountId);
    }

    public LoginResponse loginWithEmailAccount(final LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));
        validatePassword(password, user.getPassword());

        String accessToken = jwtUtils.generateToken(user, ACCESS_TOKEN_VALIDITY_PERIOD);
        String refreshToken = jwtUtils.generateToken(user, REFRESH_TOKEN_VALIDITY_PERIOD);
        return new LoginResponse(accessToken, refreshToken);
    }

    private void validatePassword(final String inputPassword, final String expectedPassword) {
        if (!passwordEncoder.matches(inputPassword, expectedPassword)) {
            throw new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL);
        }
    }

    public LoginResponse loginWithKakaoAccount(final String kakaoToken) {
        String kakaoAccountId = kakaoAuthApiService.getKakaoAccountId(kakaoToken);
        User user = userRepository.findByKakaoId(kakaoAccountId)
                .orElseThrow(() -> new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));

        String accessToken = jwtUtils.generateToken(user, ACCESS_TOKEN_VALIDITY_PERIOD);
        String refreshToken = jwtUtils.generateToken(user, REFRESH_TOKEN_VALIDITY_PERIOD);

        log.info("User login [{}]", user.getEmail());
        return new LoginResponse(accessToken, refreshToken);
    }
}
