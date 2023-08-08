package team.moca.camo.common;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Coupon;
import team.moca.camo.domain.Favorite;
import team.moca.camo.domain.UserInterface;
import team.moca.camo.domain.UserNotification;
import team.moca.camo.domain.value.UserType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class GuestUser implements UserInterface {

    private static final GuestUser INSTANCE = new GuestUser();

    private final String email = "Guest";
    private final String password = "Guest";
    private final String phone = "Guest";
    private final String nickname = "Guest";
    private final String kakaoId = "Guest";
    private final boolean withdrawn = false;
    private final UserType userType = UserType.GUEST;
    private final List<String> roles = new ArrayList<>();
    private final List<Cafe> cafes = new ArrayList<>();
    private final List<Coupon> coupons = new ArrayList<>();
    private final List<Favorite> favorites = new ArrayList<>();
    private final List<UserNotification> notifications = new ArrayList<>();

    private GuestUser() {
    }

    public static GuestUser getInstance() {
        return INSTANCE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
