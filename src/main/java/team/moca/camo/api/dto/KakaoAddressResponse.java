package team.moca.camo.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
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

    protected KakaoAddressResponse() {
    }

    @Builder
    protected KakaoAddressResponse(String regionType, String addressName, String region1depthName,
                                   String region2depthName, String region3depthName, String region4depthName) {
        this.regionType = regionType;
        this.addressName = addressName;
        this.region1depthName = region1depthName;
        this.region2depthName = region2depthName;
        this.region3depthName = region3depthName;
        this.region4depthName = region4depthName;
    }
}
