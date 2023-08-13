package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import team.moca.camo.TestUtils;
import team.moca.camo.common.GuestUser;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.response.CafeDetailsResponse;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Location;
import team.moca.camo.domain.User;
import team.moca.camo.domain.embedded.Address;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.repository.CafeLocationRepository;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@DisplayName("카페 서비스 테스트")
@ExtendWith(value = MockitoExtension.class)
class CafeServiceTest {

    @InjectMocks
    private CafeService cafeService;

    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CafeLocationRepository cafeLocationRepository;
    @Mock
    private CouponService couponService;

    @DisplayName("사용자 주변의 카페 목록을 가져올 수 있다.")
    @Test
    void getNearbyCafeListSuccess() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        // when
        when(userRepository.findWithFavoriteCafesById(testUser.getId())).thenReturn(Optional.of(testUser));

        Cafe testCafe = Cafe.builder().name("Cafe A").address(Address.builder().city("test").town("test").build()).build();
        Location testLocation = Location.builder().id(testCafe.getId()).coordinates(new GeoJsonPoint(127.1111, 37.1111)).build();
        when(cafeLocationRepository.findByCoordinatesNear(any(Point.class), any(Distance.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(
                        testLocation,
                        Location.builder().id("cafe_2").coordinates(new GeoJsonPoint(127.2222, 37.2222)).build(),
                        Location.builder().id("cafe_3").coordinates(new GeoJsonPoint(127.3333, 37.3333)).build()
                )));
        when(cafeRepository.findById(any())).thenReturn(Optional.of(testCafe));

        // then
        List<CafeListResponse> nearbyCafeList =
                cafeService.getNearbyCafeList(Coordinates.of(37.1234, 127.1234), testUser.getId(), PageDto.of(0));

        assertThat(nearbyCafeList).isNotNull();
        assertThat(nearbyCafeList.size()).isEqualTo(3);
        assertThat(nearbyCafeList.get(0).getCafeId()).isEqualTo(testCafe.getId());
    }

    @DisplayName("게스트 사용자 주변의 카페 목록을 가져올 수 있다.")
    @Test
    void getNearbyCafeListOfGuestSuccess() throws Exception {
        // given
        User testUser = GuestUser.getInstance();
        Cafe testCafe = TestUtils.getTestCafeInstance();

        // when
        when(userRepository.findWithFavoriteCafesById(testUser.getId())).thenReturn(Optional.of(testUser));
        Location testLocation = Location.builder().id(testCafe.getId()).coordinates(new GeoJsonPoint(127.1111, 37.1111)).build();
        when(cafeLocationRepository.findByCoordinatesNear(any(Point.class), any(Distance.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(
                        testLocation,
                        Location.builder().id("cafe_2").coordinates(new GeoJsonPoint(127.2222, 37.2222)).build(),
                        Location.builder().id("cafe_3").coordinates(new GeoJsonPoint(127.3333, 37.3333)).build()
                )));
        when(cafeRepository.findById(any())).thenReturn(Optional.of(testCafe));

        // then
        List<CafeListResponse> nearbyCafeList =
                cafeService.getNearbyCafeList(Coordinates.of(37.1234, 127.1234), testUser.getId(), PageDto.of(0));

        assertThat(nearbyCafeList).isNotNull();
        assertThat(nearbyCafeList.size()).isEqualTo(3);
    }

    @DisplayName("카페 상세 정보를 조회할 수 있다.")
    @Test
    void getCafeDetailsInformationSuccess() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();
        Cafe testCafe = TestUtils.getTestCafeInstance();

        // when
        when(userRepository.findWithFavoriteCafesById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(cafeRepository.findById(testCafe.getId())).thenReturn(Optional.of(testCafe));
        when(couponService.getUserStampsCountForCafe(testUser, testCafe)).thenReturn(0);

        // then
        CafeDetailsResponse cafeDetailsInformation =
                cafeService.getCafeDetailsInformation(testCafe.getId(), testUser.getId());

        assertThat(cafeDetailsInformation).isNotNull();
        assertThat(cafeDetailsInformation.getCafeId()).isEqualTo(testCafe.getId());
        Address address = testCafe.getAddress();
        assertThat(cafeDetailsInformation.getAddress())
                .isEqualTo(String.join(" ", address.getRoadAddress(), address.getAddressDetail()));
        assertThat(cafeDetailsInformation.getTags()).hasSize(0);
        assertThat(cafeDetailsInformation.getImages()).hasSize(0);
    }
}
