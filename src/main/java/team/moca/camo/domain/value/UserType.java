package team.moca.camo.domain.value;

import lombok.Getter;

@Getter
public enum UserType {

    CUSTOMER("손님 회원"),
    CAFE_OWNER("카페 사장"),
    GUEST("게스트");

    private final String description;

    UserType(String description) {
        this.description = description;
    }
}
