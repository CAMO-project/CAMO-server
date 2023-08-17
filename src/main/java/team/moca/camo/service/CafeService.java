package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import team.moca.camo.repository.projection.CafeIdProjection;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    public CafeService(CafeRepository cafeRepository, CafeLocationRepository cafeLocationRepository, UserRepository userRepository, CouponService couponService, AuthenticationUserFactory authenticationUserFactory) {
        this.cafeRepository = cafeRepository;
        this.cafeLocationRepository = cafeLocationRepository;
        this.userRepository = userRepository;
        this.couponService = couponService;
        this.authenticationUserFactory = authenticationUserFactory;
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

    private Page<Location> getNearbyCafesLocation(final Coordinates userCoordinates, final PageDto page) {
        PageRequest pageRequest = PageRequest.of(page.getCurrentPage(), DEFAULT_PAGE_LIST_SIZE);
        return cafeLocationRepository.findByCoordinatesNear(
                new Point(userCoordinates.getLongitude(), userCoordinates.getLatitude()),
                new Distance(CafeService.NEARBY_DISTANCE_KILOMETERS, Metrics.KILOMETERS), pageRequest
        );
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
        boolean isOwner = isOwnerOfCafe(requestUser, cafe);

        return CafeDetailsResponse.of(cafe, userStamps, isFavorite, isOwner);
    }

    public List<CafeListResponse> getSortedAndFilteredCafeList(
            final Coordinates userCoordinates, final SortType sortType,
            final List<String> filterTags, final String authenticatedAccountId, final PageDto page
    ) {
        User requestUser =
                authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(
                        authenticatedAccountId, userRepository::findWithFavoriteCafesById
                );

        switch (sortType) {
            case DISTANCE: {
                Page<Location> nearbyCafesLocation;
                if (filterTags != null) {
                    List<String> filteredCafeIdList = cafeRepository.findByTagsIdIn(filterTags).stream()
                            .map(CafeIdProjection::getId)
                            .collect(Collectors.toList());
                    nearbyCafesLocation = getNearbyCafesLocation(filteredCafeIdList, userCoordinates, page);
                } else {
                    nearbyCafesLocation = getNearbyCafesLocation(userCoordinates, page);
                }

                page.updateTotalPages(nearbyCafesLocation.getTotalPages());
                return getCafeListOrderByDistance(nearbyCafesLocation, requestUser);
            }
            case RATING: {
                List<String> nearbyCafeIdList = getNearbyCafeIdList(userCoordinates);
                PageRequest pageRequest =
                        PageRequest.of(page.getCurrentPage(), DEFAULT_PAGE_LIST_SIZE, Sort.by(SortType.RATING.getSortPropertyName()));

                return getCafeListResponses(filterTags, page, requestUser, nearbyCafeIdList, pageRequest);
            }
            case FAVORITE: {
                List<String> nearbyCafeIdList = getNearbyCafeIdList(userCoordinates);
                PageRequest pageRequest =
                        PageRequest.of(page.getCurrentPage(), DEFAULT_PAGE_LIST_SIZE, Sort.by(SortType.FAVORITE.getSortPropertyName()));

                return getCafeListResponses(filterTags, page, requestUser, nearbyCafeIdList, pageRequest);
            }
            default: {
                return null;
            }
        }
    }

    private List<CafeListResponse> getCafeListResponses(
            final List<String> filterTags, final PageDto page, final User requestUser,
            final List<String> nearbyCafeIdList, final PageRequest pageRequest
    ) {
        Page<Cafe> cafes;
        if (filterTags != null) {
            cafes = cafeRepository.findDistinctByIdInAndTagsIdIn(nearbyCafeIdList, filterTags, pageRequest);
        } else {
            cafes = cafeRepository.findByIdIn(nearbyCafeIdList, pageRequest);
        }
        page.updateTotalPages(cafes.getTotalPages());
        return cafes.stream()
                .map(cafe -> {
                    boolean isFavorite = isFavoriteByUser(requestUser, cafe);
                    return CafeListResponse.of(cafe, isFavorite);
                })
                .collect(Collectors.toList());
    }

    private List<String> getNearbyCafeIdList(final Coordinates userCoordinates) {
        List<Location> nearbyCafesLocation = cafeLocationRepository.findByCoordinatesNear(
                new Point(userCoordinates.getLongitude(), userCoordinates.getLatitude()),
                new Distance(NEARBY_DISTANCE_KILOMETERS, Metrics.KILOMETERS)
        );
        return nearbyCafesLocation.stream()
                .map(Location::getId)
                .collect(Collectors.toList());
    }

    private Page<Location> getNearbyCafesLocation(
            final List<String> filteredCafeIdList, final Coordinates userCoordinates, final PageDto page
    ) {
        PageRequest pageRequest = PageRequest.of(page.getCurrentPage(), DEFAULT_PAGE_LIST_SIZE);
        return cafeLocationRepository.findByIdInAndCoordinatesNear(
                filteredCafeIdList, new Point(userCoordinates.getLongitude(), userCoordinates.getLatitude()),
                new Distance(CafeService.NEARBY_DISTANCE_KILOMETERS, Metrics.KILOMETERS), pageRequest
        );
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

    private boolean isFavoriteByUser(final User user, final Cafe cafe) {
        List<Favorite> favorites = user.getFavorites();
        return favorites.stream()
                .anyMatch(favorite -> favorite.getCafe().equals(cafe));
    }

    private boolean isOwnerOfCafe(final User requestUser, final Cafe cafe) {
        return cafe.getOwner().equals(requestUser);
    }
}
