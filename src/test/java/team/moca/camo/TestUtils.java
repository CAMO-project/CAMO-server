package team.moca.camo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.moca.camo.domain.User;

public class TestUtils {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private static final User USER_INSTANCE = User.builder()
            .email("test@gmail.com")
            .password(PASSWORD_ENCODER.encode("test1234"))
            .phone("01012345678")
            .nickname("test")
            .build();

    public static User getTestUserInstance() {
        return USER_INSTANCE;
    }
}
