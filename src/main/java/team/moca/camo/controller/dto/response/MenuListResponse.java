package team.moca.camo.controller.dto.response;

import lombok.Getter;
import team.moca.camo.domain.Menu;

@Getter
public class MenuListResponse {

    private final String menuName;
    private final int menuPrice;

    protected MenuListResponse(String menuName, int menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static MenuListResponse of(Menu menu) {
        return new MenuListResponse(menu.getName(), menu.getPrice());
    }
}
