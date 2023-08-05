package team.moca.camo.controller.dto.response;

import lombok.Getter;

@Getter
public class SignUpResponse {

    private final boolean camoAccount;
    private final boolean kakaoAccount;

    public SignUpResponse(boolean camoAccount, boolean kakaoAccount) {
        this.camoAccount = camoAccount;
        this.kakaoAccount = kakaoAccount;
    }
}
