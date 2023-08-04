package team.moca.camo.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class SignUpRequest {

    @Email
    @NotBlank(message = "회원 이메일은 비어있을 수 없습니다.")
    @Size(max = 50, message = "이메일은 최대 50자까지만 입력이 가능합니다.")
    private String email;

    @NotBlank(message = "닉네임은 비어있을 수 없습니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상, 10자 이어야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 한글, 영문, 숫자만 가능합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    @Size(min = 6, max = 20, message = "비밀번호는 6자 이상, 20자 이하이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9!@#$%^&*~]+$",
            message = "비밀번호는 영어 대소문자, 숫자, 특수문자(!@#$%^&*~)만 가능합니다."
    )
    private String password;

    @NotBlank(message = "비밀번호 확인은 비어있을 수 없습니다.")
    @Size(min = 6, max = 20, message = "비밀번호 확인은 6자 이상, 20자 이하이어야 합니다.")
    private String passwordCheck;

    @NotBlank(message = "전화번호는 비어있을 수 없습니다.")
    @Size(min = 8, max = 15, message = "전화번호는 7자 이상, 15자 이하이어야 합니다.")
    @Pattern(regexp = "^[0-9]+$", message = "전화번호는 숫자만 가능합니다.")
    private String phone;

    protected SignUpRequest() {
    }

    @Builder
    protected SignUpRequest(String email, String nickname, String password, String passwordCheck, String phone) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.phone = phone;
    }
}
