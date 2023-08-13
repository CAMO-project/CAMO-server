package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Coupon;
import team.moca.camo.domain.User;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    Optional<Coupon> findByUserAndCafe(User user, Cafe cafe);
}
