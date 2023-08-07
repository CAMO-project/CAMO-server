package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, String> {

    List<Cafe> findByTown(String town);
}
