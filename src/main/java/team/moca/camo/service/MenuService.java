package team.moca.camo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.common.GuestUser;
import team.moca.camo.controller.dto.response.MenuListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Like;
import team.moca.camo.domain.Menu;
import team.moca.camo.domain.User;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;
import team.moca.camo.exception.error.ClientRequestError;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.MenuRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;

    public MenuService(MenuRepository menuRepository, CafeRepository cafeRepository, UserRepository userRepository) {
        this.menuRepository = menuRepository;
        this.cafeRepository = cafeRepository;
        this.userRepository = userRepository;
    }

    public List<MenuListResponse> getSignatureMenuListOfCafe(final String cafeId, final String authenticatedAccountId) {
        User requestUser = getAuthenticatedUserOrGuestUserWithFindOption(authenticatedAccountId, userRepository::findWithLikeMenusById);
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ClientRequestError.NON_EXISTENT_CAFE));
        List<Menu> signatureMenus = menuRepository.findByCafeAndIsSignature(cafe, true);
        return signatureMenus.stream()
                .map(menu -> {
                    boolean isLike = isLikeByUser(requestUser, menu);
                    return MenuListResponse.of(menu, isLike);
                })
                .collect(Collectors.toList());
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

    private boolean isLikeByUser(final User user, final Menu menu) {
        List<Like> likes = user.getLikes();
        return likes.stream()
                .anyMatch(like -> like.getMenu().equals(menu));
    }
}
