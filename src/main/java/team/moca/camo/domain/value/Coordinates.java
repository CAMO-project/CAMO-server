package team.moca.camo.domain.value;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Coordinates {

    @JsonAlias(value = "y")
    private final double latitude;

    @JsonAlias(value = "x")
    private final double longitude;

    protected Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinates of(double latitude, double longitude) {
        return new Coordinates(latitude, longitude);
    }
}
