package team.moca.camo.controller.dto.request;

import lombok.Getter;

@Getter
public class TokenRequest {

    private String refreshToken;

    protected TokenRequest() {
    }
}
