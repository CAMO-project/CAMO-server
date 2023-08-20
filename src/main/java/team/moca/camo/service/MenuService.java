package team.moca.camo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.response.MenuListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Like;
import team.moca.camo.domain.Menu;
import team.moca.camo.domain.User;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ClientRequestError;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.MenuRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private static final int DEFAULT_PAGE_LIST_SIZE = 10;

    private final MenuRepository menuRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final AuthenticationUserFactory authenticationUserFactory;

    public MenuService(MenuRepository menuRepository, CafeRepository cafeRepository, UserRepository userRepository, AuthenticationUserFactory authenticationUserFactory) {
        this.menuRepository = menuRepository;
        this.cafeRepository = cafeRepository;
        this.userRepository = userRepository;
        this.authenticationUserFactory = authenticationUserFactory;
    }

    public List<MenuListResponse> getSignatureMenuListOfCafe(final String cafeId, final String authenticatedAccountId) {
        User requestUser =
                authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(
                        authenticatedAccountId, userRepository::findWithLikeMenusById
                );
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ClientRequestError.NON_EXISTENT_CAFE));

        Page<Menu> signatureMenus = getSignatureMenus(cafe);
        return convertToMenuListResponseList(signatureMenus, requestUser);
    }

    private Page<Menu> getSignatureMenus(Cafe cafe) {
        return menuRepository.findByCafeAndIsSignature(cafe, true, null);
    }

    public List<MenuListResponse> getBasicMenuListOfCafe(
            final String cafeId, final String authenticatedAccountId, final PageDto page
    ) {
        User requestUser =
                authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(
                        authenticatedAccountId, userRepository::findWithLikeMenusById
                );
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ClientRequestError.NON_EXISTENT_CAFE));

        Page<Menu> basicMenus = getBasicMenus(cafe, page);
        page.updateTotalPages(basicMenus.getTotalPages());
        return convertToMenuListResponseList(basicMenus, requestUser);
    }

    private Page<Menu> getBasicMenus(final Cafe cafe, final PageDto page) {
        PageRequest pageRequest = PageRequest.of(page.getCurrentPage(), DEFAULT_PAGE_LIST_SIZE);
        return menuRepository.findByCafeAndIsSignature(cafe, false, pageRequest);
    }

    private List<MenuListResponse> convertToMenuListResponseList(final Iterable<Menu> menus, final User requestUser) {
        return StreamSupport.stream(menus.spliterator(), false)
                .map(menu -> {
                    boolean isLike = isLikeByUser(requestUser, menu);
                    return MenuListResponse.of(menu, isLike);
                })
                .collect(Collectors.toList());
    }

    private boolean isLikeByUser(final User user, final Menu menu) {
        List<Like> likes = user.getLikes();
        return likes.stream()
                .anyMatch(like -> like.getMenu().equals(menu));
    }
}
