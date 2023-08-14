package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Menu;

@Getter
public class MenuListResponse {

    private final String menuId;
    private final String menuName;
    private final int menuPrice;
    private final boolean isLike;

    @Builder
    protected MenuListResponse(String menuId, String menuName, int menuPrice, boolean isLike) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.isLike = isLike;
    }

    public static MenuListResponse of(Menu menu, boolean isLike) {
        return MenuListResponse.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .menuPrice(menu.getPrice())
                .isLike(isLike)
                .build();
    }
}
