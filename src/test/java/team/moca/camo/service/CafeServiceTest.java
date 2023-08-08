package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.moca.camo.TestUtils;
import team.moca.camo.api.KakaoLocalApiService;
import team.moca.camo.api.dto.KakaoAddressResponse;
import team.moca.camo.common.GuestUser;
import team.moca.camo.controller.dto.CafeListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.User;
import team.moca.camo.domain.embedded.Address;
import team.moca.camo.domain.value.Coordinates;
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
    private KakaoLocalApiService kakaoLocalApiService;
    @Mock
    private UserRepository userRepository;

    @DisplayName("사용자 주변의 카페 목록을 가져올 수 있다.")
    @Test
    void getNearbyCafeListSuccess() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();

        // when
        when(userRepository.findWithFavoriteCafesById(testUser.getId())).thenReturn(Optional.of(testUser));
        KakaoAddressResponse addressResponse = KakaoAddressResponse.builder()
                .region2depthName("test")
                .build();
        when(kakaoLocalApiService.coordinatesToAddress(any(Coordinates.class)))
                .thenReturn(addressResponse);

        Cafe testCafe = Cafe.builder().name("Cafe A").address(Address.builder().city("test").town("test").build()).build();
        when(cafeRepository.findByCity(addressResponse.getRegion2depthName()))
                .thenReturn(List.of(
                        testCafe,
                        Cafe.builder().name("Cafe B").address(Address.builder().city("test").town("test").build()).build(),
                        Cafe.builder().name("Cafe C").address(Address.builder().city("test").town("test").build()).build()
                ));

        // then
        List<CafeListResponse> nearbyCafeList =
                cafeService.getNearbyCafeList(Coordinates.of(37.1234, 127.1234), testUser.getId());

        assertThat(nearbyCafeList).isNotNull();
        assertThat(nearbyCafeList.size()).isEqualTo(3);
        assertThat(nearbyCafeList.get(0).getCafeId()).isEqualTo(testCafe.getId());
    }

    @DisplayName("게스트 사용자 주변의 카페 목록을 가져올 수 있다.")
    @Test
    void getNearbyCafeListOfGuestSuccess() throws Exception {
        // given
        User testUser = GuestUser.getInstance();

        // when
        when(userRepository.findWithFavoriteCafesById(testUser.getId())).thenReturn(Optional.of(testUser));
        KakaoAddressResponse addressResponse = KakaoAddressResponse.builder()
                .region2depthName("test")
                .build();
        when(kakaoLocalApiService.coordinatesToAddress(any(Coordinates.class)))
                .thenReturn(addressResponse);

        Cafe testCafe = Cafe.builder().name("Cafe A").address(Address.builder().city("test").town("test").build()).build();
        when(cafeRepository.findByCity(addressResponse.getRegion2depthName()))
                .thenReturn(List.of(
                        testCafe,
                        Cafe.builder().name("Cafe B").address(Address.builder().city("test").town("test").build()).build(),
                        Cafe.builder().name("Cafe C").address(Address.builder().city("test").town("test").build()).build()
                ));

        // then
        List<CafeListResponse> nearbyCafeList =
                cafeService.getNearbyCafeList(Coordinates.of(37.1234, 127.1234), testUser.getId());

        assertThat(nearbyCafeList).isNotNull();
        assertThat(nearbyCafeList.size()).isEqualTo(3);
    }
}
