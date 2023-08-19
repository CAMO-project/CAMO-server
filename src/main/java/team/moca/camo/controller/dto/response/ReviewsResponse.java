package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
public class ReviewsResponse {

    private final List<ReviewListDto> reviews;
    private final double ratingAverage;

    @Builder
    protected ReviewsResponse(List<ReviewListDto> reviews, double ratingAverage) {
        this.reviews = reviews;
        this.ratingAverage = ratingAverage;
    }

    public static ReviewsResponse of(List<ReviewListDto> reviews, double ratingAverage) {
        return new ReviewsResponse(reviews, ratingAverage);
    }
}

