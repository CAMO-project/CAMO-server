package team.moca.camo.controller.dto;

import lombok.Getter;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.FieldError;

@Getter
public class PageDto {

    private final int currentPage;
    private int totalPages;

    protected PageDto(int currentPage) {
        this.currentPage = currentPage;
    }

    public static PageDto of(int currentPage) {
        if (currentPage < 0) {
            throw new BusinessException(FieldError.INVALID_PAGE);
        }
        return new PageDto(currentPage);
    }

    public void updateTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
