package team.moca.camo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.controller.dto.ResponseDto;
import team.moca.camo.controller.dto.response.ReviewListResponse;
import team.moca.camo.service.ReviewService;

import java.util.List;

@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/cafe")
    public ResponseDto<List<ReviewListResponse>> reviewListOfCafe(
            @RequestParam(name = "cafe_id") String cafeId
    ) {
        List<ReviewListResponse> reviewList = reviewService.getReviewListOfCafe(cafeId);
        return ResponseDto.of(reviewList, String.format("Reviews of cafe [%s]", cafeId));
    }
}
