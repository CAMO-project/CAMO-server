package team.moca.camo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.controller.dto.response.ReviewListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Review;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ClientRequestError;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CafeRepository cafeRepository;

    public ReviewService(ReviewRepository reviewRepository, CafeRepository cafeRepository) {
        this.reviewRepository = reviewRepository;
        this.cafeRepository = cafeRepository;
    }

    public List<ReviewListResponse> getReviewListOfCafe(final String cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ClientRequestError.NON_EXISTENT_CAFE));
        List<Review> reviews = reviewRepository.findByCafe(cafe);
        return reviews.stream()
                .map(ReviewListResponse::of)
                .collect(Collectors.toList());
    }
}
