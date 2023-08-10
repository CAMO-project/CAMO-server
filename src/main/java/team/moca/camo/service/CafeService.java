package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.common.GuestUser;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Favorite;
import team.moca.camo.domain.Location;
import team.moca.camo.domain.User;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.repository.CafeLocationRepository;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CafeService {

    private static final int DEFAULT_PAGE_LIST_SIZE = 10;

    private final CafeRepository cafeRepository;
    private final CafeLocationRepository cafeLocationRepository;
    private final UserRepository userRepository;

    public CafeService(CafeRepository cafeRepository, CafeLocationRepository cafeLocationRepository, UserRepository userRepository) {
        this.cafeRepository = cafeRepository;
        this.cafeLocationRepository = cafeLocationRepository;
        this.userRepository = userRepository;
    }

    public List<CafeListResponse> getNearbyCafeList(final Coordinates userCoordinates, final String authenticatedAccountId, final PageDto page) {
        User requestUser = getAuthenticatedUserOrGuestUser(authenticatedAccountId);
        PageRequest pageRequest = PageRequest.of(page.getNowPage(), DEFAULT_PAGE_LIST_SIZE);
        Page<Location> nearbyCafesLocation = cafeLocationRepository.findByCoordinatesNear(
                new Point(userCoordinates.getLongitude(), userCoordinates.getLatitude()),
                new Distance(5, Metrics.KILOMETERS), pageRequest
        );
        page.updateTotalPages(nearbyCafesLocation.getTotalPages());

        return nearbyCafesLocation.stream()
                .map(location -> {
                    String cafeId = location.getId();
                    return cafeRepository.findById(cafeId).orElseThrow(() ->
                            new IllegalArgumentException(String.format("Cafe ID is not exists [{%s}]", cafeId)));
                })
                .map(cafe ->
                        CafeListResponse.of(cafe, isFavoriteByUser(requestUser, cafe)))
                .collect(Collectors.toList());
    }

    private User getAuthenticatedUserOrGuestUser(String authenticatedAccountId) {
        if (authenticatedAccountId == null) {
            return GuestUser.getInstance();
        } else {
            return userRepository.findWithFavoriteCafesById(authenticatedAccountId)
                    .orElseThrow(() -> new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));
        }
    }

    private boolean isFavoriteByUser(final User user, final Cafe cafe) {
        List<Favorite> favorites = user.getFavorites();
        return favorites.stream()
                .anyMatch(favorite -> favorite.getCafe().equals(cafe));
    }
}
