package team.moca.camo;

import team.moca.camo.domain.User;

public class TestUtils {

    private static final User USER_INSTANCE = User.builder()
            .email("test@gmail.com")
            .password("test1234")
            .phone("01012345678")
            .nickname("test")
            .build();

    public static User getTestUserInstance() {
        return USER_INSTANCE;
    }
}
