package team.moca.camo.domain.value;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Coordinate {

    @Column(name = "latitude", length = 15)
    private String latitude;

    @Column(name = "longitude", length = 15)
    private String longitude;

    public Coordinate() {
    }

    public Coordinate(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}