package team.moca.camo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Tag;
import team.moca.camo.domain.User;
import team.moca.camo.domain.embedded.Address;

import java.util.List;

public class TestInstanceFactory {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private TestInstanceFactory() {
    }

    public static User getTestUser() {
        return User.builder()
                .email("test@gmail.com")
                .password(PASSWORD_ENCODER.encode("test1234"))
                .phone("01012345678")
                .nickname("test")
                .kakaoId("test")
                .build();
    }

    public static Cafe getTestCafe() {
        Cafe cafe = Cafe.builder()
                .name("Cafe A")
                .contact("01012345678")
                .introduction("This is test cafe")
                .businessRegistrationNumber("5829463486")
                .address(Address.builder().city("test").town("test").build())
                .build();
        cafe.registerBy(User.builder()
                .email("cafeOwner@gmail.com")
                .password(PASSWORD_ENCODER.encode("test1234"))
                .nickname("cafeOwner")
                .phone("01087654321")
                .build());
        cafe.addTag(new Tag("TAG A"));
        cafe.addTag(new Tag("TAG B"));
        return cafe;
    }

    public static List<User> getTestUsers() {
        return List.of(
                User.builder()
                        .email("test1@gmail.com")
                        .password(PASSWORD_ENCODER.encode("test1234"))
                        .phone("01011111111")
                        .nickname("test1")
                        .kakaoId("test1")
                        .build(),
                User.builder()
                        .email("test2@gmail.com")
                        .password(PASSWORD_ENCODER.encode("test1234"))
                        .phone("01022222222")
                        .nickname("test2")
                        .kakaoId("test2")
                        .build(),
                User.builder()
                        .email("test3@gmail.com")
                        .password(PASSWORD_ENCODER.encode("test1234"))
                        .phone("0103333333")
                        .nickname("test3")
                        .kakaoId("test3")
                        .build());
    }

    public static List<Cafe> getTestCafes() {
        return List.of(
                Cafe.builder()
                        .name("Cafe A")
                        .contact("01011111111")
                        .introduction("This is test cafe 1")
                        .businessRegistrationNumber("5829463486")
                        .address(Address.builder().city("city1").town("town1").build())
                        .build(),
                Cafe.builder()
                        .name("Cafe B")
                        .contact("01022222222")
                        .introduction("This is test cafe 2")
                        .businessRegistrationNumber("5828333486")
                        .address(Address.builder().city("city2").town("town2").build())
                        .build(),
                Cafe.builder()
                        .name("Cafe C")
                        .contact("01033333333")
                        .introduction("This is test cafe 3")
                        .businessRegistrationNumber("6483649236")
                        .address(Address.builder().city("city3").town("town3").build())
                        .build());
    }
}
