package team.moca.camo.domain.value;

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
}
