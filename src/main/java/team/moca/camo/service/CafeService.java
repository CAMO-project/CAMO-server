package team.moca.camo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.common.GuestUser;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.request.CafeRequest;
import team.moca.camo.controller.dto.response.CafeDetailsResponse;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Favorite;
import team.moca.camo.domain.Location;
import team.moca.camo.domain.User;
import team.moca.camo.domain.embedded.Address;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.domain.value.UserType;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.UserTypeException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.exception.error.ClientRequestError;
import team.moca.camo.repository.CafeLocationRepository;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class CafeService {

    private static final int DEFAULT_PAGE_LIST_SIZE = 10;
    public static final int NEARBY_DISTANCE_KILOMETERS = 1;

    private final CafeRepository cafeRepository;
    private final CafeLocationRepository cafeLocationRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;

    public CafeService(CafeRepository cafeRepository, CafeLocationRepository cafeLocationRepository, UserRepository userRepository, CouponService couponService) {
        this.cafeRepository = cafeRepository;
        this.cafeLocationRepository = cafeLocationRepository;
        this.userRepository = userRepository;
        this.couponService = couponService;
    }

    public List<CafeListResponse> getNearbyCafeList(final Coordinates userCoordinates, final String authenticatedAccountId, final PageDto page) {
        User requestUser = getAuthenticatedUserOrGuestUserWithFindOption(authenticatedAccountId, userRepository::findWithFavoriteCafesById);
        PageRequest pageRequest = PageRequest.of(page.getNowPage(), DEFAULT_PAGE_LIST_SIZE);
        Page<Location> nearbyCafesLocation = cafeLocationRepository.findByCoordinatesNear(
                new Point(userCoordinates.getLongitude(), userCoordinates.getLatitude()),
                new Distance(NEARBY_DISTANCE_KILOMETERS, Metrics.KILOMETERS), pageRequest
        );
        page.updateTotalPages(nearbyCafesLocation.getTotalPages());

        return convertCafeLocationPageToCafeListResponseList(nearbyCafesLocation, requestUser);
    }

    private List<CafeListResponse> convertCafeLocationPageToCafeListResponseList(Page<Location> nearbyCafesLocation, User requestUser) {
        return nearbyCafesLocation.stream()
                .map(location -> {
                    String cafeId = location.getId();
                    return cafeRepository.findById(cafeId).orElseThrow(() ->
                            new IllegalArgumentException(String.format("Cafe ID is not exists [%s]", cafeId)));
                })
                .map(cafe ->
                        CafeListResponse.of(cafe, isFavoriteByUser(requestUser, cafe)))
                .collect(Collectors.toList());
    }

    public CafeDetailsResponse getCafeDetailsInformation(final String cafeId, final String authenticatedAccountId) {
        User requestUser = getAuthenticatedUserOrGuestUserWithFindOption(authenticatedAccountId, userRepository::findWithFavoriteCafesById);
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() ->
                new BusinessException(ClientRequestError.NON_EXISTENT_CAFE));

        int userStamps = couponService.getUserStampsCountForCafe(requestUser, cafe);
        boolean isFavorite = isFavoriteByUser(requestUser, cafe);

        return CafeDetailsResponse.of(cafe, userStamps, isFavorite);
    }

    private User getAuthenticatedUserOrGuestUserWithFindOption(
            final String authenticatedAccountId, final Function<String, Optional<User>> findUserFunction
    ) {
        if (authenticatedAccountId == null) {
            return GuestUser.getInstance();
        } else {
            return findUserFunction.apply(authenticatedAccountId)
                    .orElseThrow(() -> new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));
        }
    }

    private boolean isFavoriteByUser(final User user, final Cafe cafe) {
        List<Favorite> favorites = user.getFavorites();
        return favorites.stream()
                .anyMatch(favorite -> favorite.getCafe().equals(cafe));
    }

    public void createCafe(CafeRequest cafeRequest) {

        //주소 넘겨받을때 어떻게 넘겨받죠?
        //아래처럼 하나씩 넘겨받는거밖에 방법 없나요?
        Address address = Address.builder()
                .state(cafeRequest.getState())
                .city(cafeRequest.getCity())
                .town(cafeRequest.getTown())
                .addressDetail(cafeRequest.getAddressDetail())
                .roadAddress(cafeRequest.getRoadAddress())
                .build();

        Cafe cafe = Cafe.builder()
                .name(cafeRequest.getName())
                .contact(cafeRequest.getContact())
                .address(address)
                .businessRegistrationNumber(cafeRequest.getBusinessRegistrationNumber())
                .build();

        /**
         * 회원 그냥 가져다 쓰는거라서 회원 아이디 검증 따로 만들어야해용
         * 일단은 cafeRequest 에 있는 회원 아이디 쓰겠습니다
         */
        User user = userRepository.findById(cafeRequest.getUserId()).get();
        cafe.CreateCafeOwner(user);

        //손님 회원이면 에러던짐
        if (user.getUserType() == UserType.CUSTOMER && user.getUserType() == UserType.CAFE_OWNER) {
            user.changeUserType(UserType.CAFE_OWNER);
        } else {
            throw new UserTypeException("손님 회원은 카페를 생성할 수 없습니다.");
        }

        cafeRepository.save(cafe);

    }
}
