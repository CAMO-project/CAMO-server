package team.moca.camo.controller.dto.request;

import lombok.Getter;
import team.moca.camo.domain.embedded.Address;

@Getter
public class CafeRequest {
    private String cafeName;
    private Address address;
    private String contact;
    private String businessRegistrationNumber;

    protected CafeRequest() {

    }
}
