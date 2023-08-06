package team.moca.camo.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequest {

    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password;
}
