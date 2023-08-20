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
import team.moca.camo.domain.BaseEntity;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CafeService {

    private static final int DEFAULT_PAGE_LIST_SIZE = 10;
    private static final double NEARBY_DISTANCE_KILOMETERS = 2;
    private static final int INFINITY_PAGE = 100_000;
    private static final int START_PAGE = 0;

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

        List<Cafe> nearbyCafes = getCafesFromCafesLocation(nearbyCafesLocation);
        return convertToCafeListResponse(nearbyCafes, requestUser);
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
                return getSortedAndFilteredCafesByDistance(userCoordinates, filterTags, requestUser, page);
            }
            case RATING: {
                return getSortedAndFilteredCafesByRating(userCoordinates, filterTags, requestUser, page);
            }
            case FAVORITE: {
                return getSortedAndFilteredCafesByFavorite(userCoordinates, filterTags, requestUser, page);
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    private List<CafeListResponse> getSortedAndFilteredCafesByDistance(
            final Coordinates userCoordinates, final List<String> filterTags,
            final User requestUser, final PageDto page
    ) {
        Page<Location> cafesLocation;
        PageRequest pageRequest = PageRequest.of(page.getCurrentPage(), DEFAULT_PAGE_LIST_SIZE);
        if (filterTags != null) {
            List<String> filteredCafeIdList = getFilteredCafeIdList(userCoordinates, filterTags);
            cafesLocation = cafeLocationRepository.findByIdInAndCoordinatesNear(
                    filteredCafeIdList, new Point(userCoordinates.getLongitude(), userCoordinates.getLatitude()),
                    new Distance(NEARBY_DISTANCE_KILOMETERS, Metrics.KILOMETERS), pageRequest
            );
        } else {
            cafesLocation = cafeLocationRepository.findByCoordinatesNear(
                    new Point(userCoordinates.getLongitude(), userCoordinates.getLatitude()),
                    new Distance(NEARBY_DISTANCE_KILOMETERS, Metrics.KILOMETERS), pageRequest
            );
        }

        page.updateTotalPages(cafesLocation.getTotalPages());
        List<Cafe> sortedAndFilteredCafes = getCafesFromCafesLocation(cafesLocation);
        return convertToCafeListResponse(sortedAndFilteredCafes, requestUser);
    }

    private List<String> getFilteredCafeIdList(final Coordinates userCoordinates, final List<String> filterTags) {
        List<String> nearbyCafeIdList = getNearbyCafeIdList(userCoordinates);
        PageRequest pageRequest = PageRequest.of(START_PAGE, INFINITY_PAGE);
        Page<Cafe> filteredCafes =
                cafeRepository.findDistinctByIdInAndTagsIdIn(nearbyCafeIdList, filterTags, pageRequest);
        return filteredCafes.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

    private List<CafeListResponse> getSortedAndFilteredCafesByRating(
            final Coordinates userCoordinates, final List<String> filterTags,
            final User requestUser, final PageDto page
    ) {
        PageRequest pageRequest =
                PageRequest.of(page.getCurrentPage(), DEFAULT_PAGE_LIST_SIZE,
                        Sort.by(Sort.Direction.DESC, SortType.RATING.getSortPropertyName()));
        return getSortedAndFilteredCafes(userCoordinates, filterTags, requestUser, page, pageRequest);
    }

    private List<CafeListResponse> getSortedAndFilteredCafesByFavorite(
            final Coordinates userCoordinates, final List<String> filterTags,
            final User requestUser, final PageDto page
    ) {
        PageRequest pageRequest =
                PageRequest.of(page.getCurrentPage(), DEFAULT_PAGE_LIST_SIZE,
                        Sort.by(Sort.Direction.DESC, SortType.FAVORITE.getSortPropertyName()));
        return getSortedAndFilteredCafes(userCoordinates, filterTags, requestUser, page, pageRequest);
    }

    private List<CafeListResponse> getSortedAndFilteredCafes(
            final Coordinates userCoordinates, final List<String> filterTags,
            final User requestUser, final PageDto page, final PageRequest pageRequest
    ) {
        List<String> nearbyCafeIdList = getNearbyCafeIdList(userCoordinates);

        Page<Cafe> sortedAndFilteredCafes;
        if (filterTags != null) {
            sortedAndFilteredCafes =
                    cafeRepository.findDistinctByIdInAndTagsIdIn(nearbyCafeIdList, filterTags, pageRequest);
        } else {
            sortedAndFilteredCafes =
                    cafeRepository.findByIdIn(nearbyCafeIdList, pageRequest);
        }

        page.updateTotalPages(sortedAndFilteredCafes.getTotalPages());
        return convertToCafeListResponse(sortedAndFilteredCafes, requestUser);
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

    private List<Cafe> getCafesFromCafesLocation(final Page<Location> cafesLocation) {
        return cafesLocation.stream()
                .map(location -> {
                    String cafeId = location.getId();
                    return cafeRepository.findById(cafeId).orElseThrow(() ->
                            new IllegalArgumentException(String.format("Cafe ID is not exists [%s]", cafeId)));
                })
                .collect(Collectors.toList());
    }

    private List<CafeListResponse> convertToCafeListResponse(final Iterable<Cafe> cafes, final User requestUser) {
        return StreamSupport.stream(cafes.spliterator(), false)
                .map(cafe -> CafeListResponse.of(cafe, isFavoriteByUser(requestUser, cafe)))
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
