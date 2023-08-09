package team.moca.camo.controller.dto;

import lombok.Getter;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.FieldError;

@Getter
public class PageDto {

    private final int nowPage;
    private int totalPages;

    protected PageDto(int nowPage) {
        this.nowPage = nowPage;
    }

    public static PageDto of(int nowPage) {
        if (nowPage < 0) {
            throw new BusinessException(FieldError.INVALID_PAGE);
        }
        return new PageDto(nowPage);
    }

    public void updateTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
