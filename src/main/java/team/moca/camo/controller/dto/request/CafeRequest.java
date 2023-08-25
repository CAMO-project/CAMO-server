package team.moca.camo.controller.dto.request;

import lombok.Getter;
import team.moca.camo.domain.User;
import team.moca.camo.domain.embedded.Address;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;

@Getter
public class CafeRequest {

    @NotBlank(message = "카페 이름은 비어있을 수 없습니다.")
    private String name;

    @NotBlank(message = "카페 주소는 비어있을 수 없습니다.")
    private String state;
    private String city;
    private String town;
    private String addressDetail;
    private String roadAddress;

    @NotBlank(message = "카페 연락처는 비어있을 수 없습니다.")
    private String contact;

    @NotBlank(message = "사업자 등록 번호는 비어있을 수 없습니다.")
    private String businessRegistrationNumber;

    @NotBlank(message = "카페소개는 비어있을 수 없습니다.")
    private String introduction;

    //토큰에서 받아와야 하는 유저 Id
    private String userId;
}