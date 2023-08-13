package team.moca.camo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.User;
import team.moca.camo.domain.embedded.Address;

public class TestUtils {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private TestUtils() {
    }

    private static final User USER_INSTANCE = User.builder()
            .email("test@gmail.com")
            .password(PASSWORD_ENCODER.encode("test1234"))
            .phone("01012345678")
            .nickname("test")
            .kakaoId("test")
            .build();

    private static final Cafe CAFE_INSTANCE = Cafe.builder()
            .name("Cafe A")
            .contact("01012345678")
            .introduction("This is test cafe")
            .businessRegistrationNumber("5829463486")
            .address(Address.builder().city("test").town("test").build())
            .build();

    public static User getTestUserInstance() {
        return USER_INSTANCE;
    }

    public static Cafe getTestCafeInstance() {
        return CAFE_INSTANCE;
    }
}
