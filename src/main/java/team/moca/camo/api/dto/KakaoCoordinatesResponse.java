package team.moca.camo.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import team.moca.camo.domain.value.Coordinates;

@Getter
public class KakaoCoordinatesResponse {

    @JsonAlias(value = "road_address")
    private Coordinates coordinates;
}
