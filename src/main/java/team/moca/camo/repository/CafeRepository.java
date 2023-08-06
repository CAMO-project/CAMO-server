package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.moca.camo.domain.Cafe;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, String> {
    List<Cafe> findByNameContaining(String searchKeyword);
}
