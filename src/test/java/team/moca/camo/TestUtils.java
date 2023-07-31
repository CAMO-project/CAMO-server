package team.moca.camo;

import team.moca.camo.domain.User;

public class TestUtils {

    public static User createTestUser() {
        return User.builder()
                .email("test@gmail.com")
                .password("test1234")
                .phone("010-9517-1530")
                .build();
    }
}
