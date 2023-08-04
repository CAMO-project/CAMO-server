package team.moca.camo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.controller.dto.ResponseDto;
import team.moca.camo.controller.dto.request.EmailDuplicateRequest;
import team.moca.camo.controller.dto.request.PhoneVerifyRequest;
import team.moca.camo.controller.dto.request.TokenRequest;
import team.moca.camo.controller.dto.response.EmailDuplicateResponse;
import team.moca.camo.controller.dto.response.TokenResponse;
import team.moca.camo.service.AuthenticationService;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/auth")
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

    @PostMapping("/email/duplication")
    public ResponseDto<EmailDuplicateResponse> checkEmailDuplicate(
            @Valid @RequestBody EmailDuplicateRequest emailDuplicateRequest
    ) {
        String validEmail = authenticationService.checkEmailDuplicate(emailDuplicateRequest.getEmail());
        return ResponseDto.of(new EmailDuplicateResponse(validEmail));
    }
}
