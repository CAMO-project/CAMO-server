package team.moca.camo.controller.dto;

import lombok.Getter;

@Getter
public class TokenRequest {

    private String refreshToken;

    protected TokenRequest() {
    }
}
