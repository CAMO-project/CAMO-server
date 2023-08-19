package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Review;
import team.moca.camo.domain.User;

import java.time.LocalDateTime;

@Getter
public class ReviewListDto {

    private final String reviewId;
    private final String reviewContents;
    private final int starRating;
    private final String writer;
    private final LocalDateTime writtenAt;

    @Builder
    ReviewListDto(String reviewId, String reviewContents, int starRating, String writer, LocalDateTime writtenAt) {
        this.reviewId = reviewId;
        this.reviewContents = reviewContents;
        this.starRating = starRating;
        this.writer = writer;
        this.writtenAt = writtenAt;
    }

    public static ReviewListDto of(Review review) {
        User writer = review.getWriter();
        String writerName;
        if (writer == null) {
            writerName = "알 수 없음";
        } else {
            writerName = writer.getNickname();
        }
        return ReviewListDto.builder()
                .reviewId(review.getId())
                .reviewContents(review.getContents())
                .starRating(review.getStarRating())
                .writer(writerName)
                .writtenAt(review.getCreatedAt())
                .build();
    }
}
