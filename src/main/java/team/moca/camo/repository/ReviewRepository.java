package team.moca.camo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.common.EntityGraphNames;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {

    @EntityGraph(value = EntityGraphNames.REVIEW_WRITER)
    List<Review> findByCafe(Cafe cafe);
}
