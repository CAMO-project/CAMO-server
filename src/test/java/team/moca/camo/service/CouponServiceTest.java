package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.moca.camo.TestInstanceFactory;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.User;
import team.moca.camo.repository.CouponRepository;

@Slf4j
@DisplayName("쿠폰 서비스 테스트")
@ExtendWith(value = MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @DisplayName("회원의 특정 카페 쿠폰의 스탬프 개수를 조회할 수 있다.")
    @Test
    void getUserStampsCountForCafeSuccess() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        Cafe testCafe = TestInstanceFactory.getTestCafeInstance();

        // when

        // then
        int userStampsCountForCafe = couponService.getUserStampsCountForCafe(testUser, testCafe);


    }
}