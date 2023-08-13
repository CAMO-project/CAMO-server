package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Coupon;
import team.moca.camo.domain.User;
import team.moca.camo.repository.CouponRepository;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public int getUserStampsCountForCafe(final User user, final Cafe cafe) {
        return couponRepository.findByUserAndCafe(user, cafe)
                .map(Coupon::getStamps)
                .orElse(0);
    }
}
