package team.moca.camo.controller.dto.response;

import lombok.Getter;

@Getter
public class KakaoIntegrationResponse {

    private final boolean kakaoAccount;

    public KakaoIntegrationResponse(boolean kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }
}
