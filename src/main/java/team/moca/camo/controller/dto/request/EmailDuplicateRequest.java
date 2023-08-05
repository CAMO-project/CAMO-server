package team.moca.camo.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class EmailDuplicateRequest {

    @Email
    @NotBlank
    private String email;
}
