package team.moca.camo.controller.dto.response;

import lombok.Getter;

@Getter
public class SignUpResponse {

    private final String accountId;

    public SignUpResponse(String accountId) {
        this.accountId = accountId;
    }
}
