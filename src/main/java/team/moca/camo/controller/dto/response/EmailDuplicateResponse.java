package team.moca.camo.controller.dto.response;

import lombok.Getter;

@Getter
public class EmailDuplicateResponse {

    private final String validEmail;

    public EmailDuplicateResponse(String validEmail) {
        this.validEmail = validEmail;
    }
}
