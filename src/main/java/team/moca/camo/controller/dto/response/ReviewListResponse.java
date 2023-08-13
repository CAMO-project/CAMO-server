package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import team.moca.camo.domain.Review;
import team.moca.camo.domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Getter
public class ReviewListResponse {

    private final String reviewContents;
    private final int starRating;
    private final String writer;
    private final LocalDateTime writtenAt;

    @Builder
    protected ReviewListResponse(String reviewContents, int starRating, String writer, LocalDateTime writtenAt) {
        this.reviewContents = reviewContents;
        this.starRating = starRating;
        this.writer = writer;
        this.writtenAt = writtenAt;
    }

    public static ReviewListResponse of(Review review) {
        User writer = review.getWriter();
        String writerName;
        if (writer == null) {
            writerName = "알 수 없음";
        } else {
            writerName = writer.getNickname();
        }
        LocalDateTime createdAt = review.getCreatedAt();
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.KOREA).format(createdAt);
        log.info("format = {}", format);
        return ReviewListResponse.builder()
                .reviewContents(review.getContents())
                .starRating(review.getStarRating())
                .writer(writerName)
                .writtenAt(review.getCreatedAt())
                .build();
    }
}
