package team.moca.camo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.controller.dto.TokenRequest;
import team.moca.camo.controller.dto.TokenResponse;
import team.moca.camo.controller.response.ResponseDto;
import team.moca.camo.service.AuthenticationService;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/token")
    public ResponseDto<TokenResponse> requestNewAccessToken(@RequestBody TokenRequest tokenRequest) {
        String accessToken = authenticationService.getNewAccessToken(tokenRequest.getRefreshToken());
        return ResponseDto.of(new TokenResponse(accessToken), "A new access token has been issued.");
    }
}
