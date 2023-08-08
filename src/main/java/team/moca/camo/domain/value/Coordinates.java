package team.moca.camo.domain.value;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Coordinates {

    @JsonAlias(value = "y")
    private double latitude;

    @JsonAlias(value = "x")
    private double longitude;
}
