package team.moca.camo.domain.embedded;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@Embeddable
public class Location {

    @Embedded
    private Address address;

    @Embedded
    private Coordinate coordinate;

    protected Location() {
    }

    @Builder
    protected Location(Address address, Coordinate coordinate) {
        this.address = address;
        this.coordinate = coordinate;
    }
}
