package team.moca.camo.common;

import lombok.Getter;
import team.moca.camo.domain.User;

@Getter
public class GuestUser {

    private static final User INSTANCE = User.builder()
            .email("guest@gmail.com")
            .password("guest1234")
            .phone("01012345678")
            .nickname("Guest")
            .kakaoId("12345678")
            .build();

    private GuestUser() {
    }

    public static User getInstance() {
        return INSTANCE;
    }
}
