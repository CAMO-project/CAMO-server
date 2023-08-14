package team.moca.camo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.common.annotation.Authenticate;
import team.moca.camo.controller.dto.ResponseDto;
import team.moca.camo.controller.dto.response.MenuListResponse;
import team.moca.camo.service.MenuService;

import java.util.List;

@RequestMapping("/api/menus")
@RestController
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/signature")
    public ResponseDto<List<MenuListResponse>> signatureMenusOfCafe(
            @Authenticate(required = false) String authenticatedAccountId,
            @RequestParam(name = "cafe_id") String cafeId
    ) {
        List<MenuListResponse> signatureMenuList = menuService.getSignatureMenuListOfCafe(cafeId, authenticatedAccountId);
        return ResponseDto.of(signatureMenuList, String.format("Signature menus of cafe [%s]", cafeId));
    }
}
