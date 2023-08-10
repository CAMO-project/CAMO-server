package team.moca.camo.domain.embedded;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Address {

    @Column(name = "state", nullable = false, length = 20)
    private String state;

    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Column(name = "town", nullable = false, length = 20)
    private String town;

    @Column(name = "address_detail", nullable = false, length = 50)
    private String addressDetail;

    @Column(name = "road_address", nullable = false, length = 100)
    private String roadAddress;

    protected Address() {
    }

    @Builder
    protected Address(String state, String city, String town, String addressDetail, String roadAddress) {
        this.state = state;
        this.city = city;
        this.town = town;
        this.addressDetail = addressDetail;
        this.roadAddress = roadAddress;
    }
}
