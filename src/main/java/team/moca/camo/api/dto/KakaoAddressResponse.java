package team.moca.camo.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

@Getter
public class KakaoAddressResponse {

    private String regionType;

    private String addressName;

    @JsonAlias(value = "region_1depth_name")
    private String region1depthName;

    @JsonAlias(value = "region_2depth_name")
    private String region2depthName;

    @JsonAlias(value = "region_3depth_name")
    private String region3depthName;

    @JsonAlias(value = "region_4depth_name")
    private String region4depthName;
}
