package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import team.moca.camo.TestInstanceFactory;
import team.moca.camo.common.GuestUser;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.response.CafeDetailsResponse;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.controller.dto.value.SortType;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Location;
import team.moca.camo.domain.Tag;
import team.moca.camo.domain.User;
import team.moca.camo.domain.embedded.Address;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.repository.CafeLocationRepository;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    private CafeLocationRepository cafeLocationRepository;
    @Mock
    private CouponService couponService;
    @Mock
    private AuthenticationUserFactory authenticationUserFactory;
    @Mock
    private UserRepository userRepository;

    @DisplayName("사용자 주변의 카페 목록을 가져올 수 있다.")
    @Test
    void getNearbyCafeListSuccess() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();

        // when
        when(authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(eq(testUser.getId()), any(Function.class)))
                .thenReturn(testUser);

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
        Cafe testCafe = TestInstanceFactory.getTestCafeInstance();

        // when
        when(authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(eq(testUser.getId()), any(Function.class)))
                .thenReturn(testUser);
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
        User testUser = TestInstanceFactory.getTestUserInstance();
        Cafe testCafe = TestInstanceFactory.getTestCafeInstance();
        testCafe.registerBy(testUser);

        // when
        when(authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(eq(testUser.getId()), any(Function.class)))
                .thenReturn(testUser);
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

    @DisplayName("존재하지 않는 cafe_id의 경우 조회에 실패한다.")
    @Test
    void getCafeDetailsInformationFailNonExistentId() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        Cafe testCafe = TestInstanceFactory.getTestCafeInstance();

        // when
        when(cafeRepository.findById(anyString())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> cafeService.getCafeDetailsInformation(testCafe.getId(), testUser.getId()))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("카페 목록을 정렬하여 조회할 수 있다.")
    @Test
    void getSortedCafeListSuccess() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        Cafe testCafe = TestInstanceFactory.getTestCafeInstance();

        testCafe.registerBy(testUser);
        Tag tag1 = new Tag("Test Tag 1");
        Tag tag2 = new Tag("Test Tag 2");
        testCafe.addTag(tag1);
        testCafe.addTag(tag2);

        // when
        when(authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(eq(testUser.getId()), any(Function.class)))
                .thenReturn(testUser);
        when(cafeLocationRepository.findByCoordinatesNear(any(Point.class), any(Distance.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(Location.builder().id(testCafe.getId()).build()), PageRequest.of(0, 10), 10));
        when(cafeRepository.findById(testCafe.getId())).thenReturn(Optional.of(testCafe));

        // then
        List<CafeListResponse> cafeListSortedByDistance = cafeService.getSortedAndFilteredCafeList(
                Coordinates.of(37.000000, 127.000000), SortType.DISTANCE,
                null, testUser.getId(), PageDto.of(0)
        );

        assertThat(cafeListSortedByDistance).isNotNull();
        assertThat(cafeListSortedByDistance.get(0).getCafeId()).isEqualTo(testCafe.getId());
    }
}
