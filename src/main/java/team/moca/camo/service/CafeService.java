package team.moca.camo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.api.KakaoLocalApiService;
import team.moca.camo.api.dto.KakaoAddressResponse;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.response.CafeDetailsResponse;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.controller.dto.value.SortType;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Favorite;
import team.moca.camo.domain.Location;
import team.moca.camo.domain.User;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ClientRequestError;
import team.moca.camo.repository.CafeLocationRepository;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class CafeService {

    private static final int DEFAULT_PAGE_LIST_SIZE = 10;
    private static final double NEARBY_DISTANCE_KILOMETERS = 3;

    private final CafeRepository cafeRepository;
    private final CafeLocationRepository cafeLocationRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;
    private final AuthenticationUserFactory authenticationUserFactory;
    private final KakaoLocalApiService kakaoLocalApiService;

    public CafeService(CafeRepository cafeRepository, CafeLocationRepository cafeLocationRepository, UserRepository userRepository, CouponService couponService, AuthenticationUserFactory authenticationUserFactory, KakaoLocalApiService kakaoLocalApiService) {
        this.cafeRepository = cafeRepository;
        this.cafeLocationRepository = cafeLocationRepository;
        this.userRepository = userRepository;
        this.couponService = couponService;
        this.authenticationUserFactory = authenticationUserFactory;
        this.kakaoLocalApiService = kakaoLocalApiService;
    }

    public List<CafeListResponse> getNearbyCafeList(
            final Coordinates userCoordinates, final String authenticatedAccountId, final PageDto page
    ) {
        User requestUser =
                authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(
                        authenticatedAccountId, userRepository::findWithFavoriteCafesById
                );
        Page<Location> nearbyCafesLocation = getNearbyCafesLocation(userCoordinates, page);
        page.updateTotalPages(1);

        return getCafeListOrderByDistance(nearbyCafesLocation, requestUser);
    }

    public CafeDetailsResponse getCafeDetailsInformation(final String cafeId, final String authenticatedAccountId) {
        User requestUser =
                authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(
                        authenticatedAccountId, userRepository::findWithFavoriteCafesById
                );
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() ->
                new BusinessException(ClientRequestError.NON_EXISTENT_CAFE));

        int userStamps = couponService.getUserStampsCountForCafe(requestUser, cafe);
        boolean isFavorite = isFavoriteByUser(requestUser, cafe);

        return CafeDetailsResponse.of(cafe, userStamps, isFavorite);
    }

    public List<CafeListResponse> getSortedCafeList(
            final Coordinates userCoordinates, final SortType sortType,
            final String authenticatedAccountId, final PageDto page
    ) {
        User requestUser =
                authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(
                        authenticatedAccountId, userRepository::findWithFavoriteCafesById
                );

        switch (sortType) {
            case DISTANCE: {
                Page<Location> nearbyCafesLocation = getNearbyCafesLocation(userCoordinates, page);
                page.updateTotalPages(nearbyCafesLocation.getTotalPages());
                return getCafeListOrderByDistance(nearbyCafesLocation, requestUser);
            }
            case RATING: {
                KakaoAddressResponse userAddress = kakaoLocalApiService.coordinatesToAddress(userCoordinates);
                return getCafeListOrderByRating(userAddress.getRegion2depthName(), requestUser, page);
            }
            case FAVORITE: {
                KakaoAddressResponse userAddress = kakaoLocalApiService.coordinatesToAddress(userCoordinates);
                return getCafeListOrderByFavoritesCount(userAddress.getRegion2depthName(), requestUser, page);
            }
            default: {
                return null;
            }
        }
    }

    private List<CafeListResponse> getCafeListOrderByDistance(final Page<Location> cafesLocation, final User requestUser) {
        return cafesLocation.stream()
                .map(location -> {
                    String cafeId = location.getId();
                    return cafeRepository.findById(cafeId).orElseThrow(() ->
                            new IllegalArgumentException(String.format("Cafe ID is not exists [%s]", cafeId)));
                })
                .map(cafe ->
                        CafeListResponse.of(cafe, isFavoriteByUser(requestUser, cafe)))
                .collect(Collectors.toList());
    }

    private Page<Location> getNearbyCafesLocation(final Coordinates userCoordinates, final PageDto page) {
        PageRequest pageRequest = PageRequest.of(page.getNowPage(), DEFAULT_PAGE_LIST_SIZE);
        return cafeLocationRepository.findByCoordinatesNear(
                new Point(userCoordinates.getLongitude(), userCoordinates.getLatitude()),
                new Distance(CafeService.NEARBY_DISTANCE_KILOMETERS, Metrics.KILOMETERS), pageRequest
        );
    }

    private List<CafeListResponse> getCafeListOrderByRating(final String userCity, final User requestUser, final PageDto page) {
        return getSortedCafePage(SortType.RATING, userCity, page).stream()
                .map(cafe -> CafeListResponse.of(cafe, isFavoriteByUser(requestUser, cafe)))
                .collect(Collectors.toList());
    }

    private List<CafeListResponse> getCafeListOrderByFavoritesCount(final String userCity, final User requestUser, final PageDto page) {
        return getSortedCafePage(SortType.FAVORITE, userCity, page).stream()
                .map(cafe -> CafeListResponse.of(cafe, isFavoriteByUser(requestUser, cafe)))
                .collect(Collectors.toList());
    }

    private Page<Cafe> getSortedCafePage(final SortType sortType, final String userCity, final PageDto page) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortType.getSortPropertyName());
        PageRequest pageRequest = PageRequest.of(page.getNowPage(), DEFAULT_PAGE_LIST_SIZE, sort);

        Page<Cafe> userTownCafes = cafeRepository.findByCity(userCity, pageRequest);
        page.updateTotalPages(userTownCafes.getTotalPages());
        return userTownCafes;
    }

    private boolean isFavoriteByUser(final User user, final Cafe cafe) {
        List<Favorite> favorites = user.getFavorites();
        return favorites.stream()
                .anyMatch(favorite -> favorite.getCafe().equals(cafe));
    }
}
