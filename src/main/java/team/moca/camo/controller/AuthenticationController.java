package team.moca.camo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.common.annotation.Authenticate;
import team.moca.camo.controller.dto.ResponseDto;
import team.moca.camo.controller.dto.request.EmailDuplicateRequest;
import team.moca.camo.controller.dto.request.KakaoAccountRequest;
import team.moca.camo.controller.dto.request.LoginRequest;
import team.moca.camo.controller.dto.request.PhoneVerifyRequest;
import team.moca.camo.controller.dto.request.SignUpRequest;
import team.moca.camo.controller.dto.request.TokenRequest;
import team.moca.camo.controller.dto.response.EmailDuplicateResponse;
import team.moca.camo.controller.dto.response.KakaoIntegrationResponse;
import team.moca.camo.controller.dto.response.LoginResponse;
import team.moca.camo.controller.dto.response.SignUpResponse;
import team.moca.camo.controller.dto.response.TokenResponse;
import team.moca.camo.service.AuthenticationService;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/authentication")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/token")
    public ResponseDto<TokenResponse> requestNewAccessToken(@RequestBody TokenRequest tokenRequest) {
        String accessToken = authenticationService.getNewAccessToken(tokenRequest.getRefreshToken());
        return ResponseDto.of(new TokenResponse(accessToken), "A new access token has been issued.");
    }

    @GetMapping("/phone")
    public ResponseDto<Object> requestPhoneVerificationCode(@Valid @ModelAttribute PhoneVerifyRequest phoneVerifyRequest) {
        log.info("phoneNumber = {}", phoneVerifyRequest.getPhoneNumber());
        authenticationService.sendVerificationCodeMessage(phoneVerifyRequest.getPhoneNumber());
        return ResponseDto.of("Phone verification code has been sent.");
    }

    @PostMapping("/email")
    public ResponseDto<EmailDuplicateResponse> checkEmailDuplicate(
            @Valid @RequestBody EmailDuplicateRequest emailDuplicateRequest
    ) {
        String validEmail = authenticationService.validateEmail(emailDuplicateRequest.getEmail());
        return ResponseDto.of(new EmailDuplicateResponse(validEmail), "Email available.");
    }

    @PostMapping("/signup")
    public ResponseDto<SignUpResponse> signUpByEmail(@Valid @RequestBody SignUpRequest signUpRequest) {
        String accountId = authenticationService.createNewEmailAccount(signUpRequest);
        log.info("Sign-up [{}]", accountId);
        SignUpResponse signUpResponse = new SignUpResponse(accountId);
        return ResponseDto.of(signUpResponse, "A new email account has been created.");
    }

    @PostMapping("/kakao")
    public ResponseDto<KakaoIntegrationResponse> integrateKakaoAccount(
            @Authenticate String authenticatedAccountId,
            @Valid @RequestBody KakaoAccountRequest kakaoAccountRequest
    ) {
        authenticationService.integrateKakaoAccountWithEmailAccount(authenticatedAccountId,
                kakaoAccountRequest.getKakaoToken());
        log.info("Kakao account integration [{}]", authenticatedAccountId);
        return ResponseDto.of(new KakaoIntegrationResponse(true), "Kakao account has been integrated.");
    }

    @PostMapping("/login")
    public ResponseDto<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.loginWithEmailAccount(loginRequest);
        log.info("User login [{}]", loginRequest.getEmail());
        return ResponseDto.of(loginResponse, "You have successfully logged in.");
    }
}
