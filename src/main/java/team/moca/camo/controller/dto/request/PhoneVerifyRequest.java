package team.moca.camo.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class PhoneVerifyRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9]+$", message = "전화번호는 [01012345678] 형식이어야 합니다.")
    private final String phoneNumber;

    public PhoneVerifyRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
