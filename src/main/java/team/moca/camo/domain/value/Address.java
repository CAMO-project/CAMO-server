package team.moca.camo.domain.value;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Address {

    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Column(name = "town", nullable = false, length = 20)
    private String town;

    @Column(name = "street", nullable = false, length = 20)
    private String street;

    @Column(name = "address_detail", nullable = false, length = 50)
    private String addressDetail;

    @Column(name = "road_address", nullable = false, length = 120)
    private String roadAddress;
}
