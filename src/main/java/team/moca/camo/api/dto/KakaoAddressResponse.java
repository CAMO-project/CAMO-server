package team.moca.camo.api.dto;

import lombok.Getter;

@Getter
public class KakaoAddressResponse {

    private String regionType;

    private String addressName;

    private String region1depthName;

    private String region2depthName;

    private String region3depthName;

    private String region4depthName;
}
