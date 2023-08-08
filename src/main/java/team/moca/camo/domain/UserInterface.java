package team.moca.camo.domain;

import org.springframework.security.core.userdetails.UserDetails;
import team.moca.camo.domain.value.UserType;

import java.util.List;

public interface UserInterface extends UserDetails {

    String getPhone();

    String getNickname();

    String getKakaoId();

    boolean isWithdrawn();

    UserType getUserType();

    List<Cafe> getCafes();

    List<Coupon> getCoupons();

    List<Favorite> getFavorites();

    List<UserNotification> getNotifications();
}
