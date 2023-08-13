package team.moca.camo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.controller.dto.response.MenuListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Menu;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ClientRequestError;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.MenuRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final CafeRepository cafeRepository;

    public MenuService(MenuRepository menuRepository, CafeRepository cafeRepository) {
        this.menuRepository = menuRepository;
        this.cafeRepository = cafeRepository;
    }

    public List<MenuListResponse> getSignatureMenusOfCafe(final String cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ClientRequestError.NON_EXISTENT_CAFE));
        List<Menu> signatureMenus = menuRepository.findByCafeAndIsSignature(cafe, true);
        return signatureMenus.stream()
                .map(MenuListResponse::of)
                .collect(Collectors.toList());
    }
}
