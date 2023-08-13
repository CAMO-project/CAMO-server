package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;

public interface CafeRepository extends JpaRepository<Cafe, String> {
}
