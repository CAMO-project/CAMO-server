package team.moca.camo.controller.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class KakaoAccountRequest {

    @NotBlank(message = "카카오 액세스 토큰은 비어있을 수 없습니다.")
    private String kakaoToken;
}
