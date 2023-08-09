package team.moca.camo.controller.dto;

import lombok.Getter;

@Getter
public class PageDto {

    private final int nowPage;
    private int maxPage;

    protected PageDto(int nowPage) {
        this.nowPage = nowPage;
    }

    public static PageDto of(int nowPage) {
        return new PageDto(nowPage);
    }

    public void updateMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
}
