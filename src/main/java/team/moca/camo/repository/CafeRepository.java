package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.moca.camo.domain.Cafe;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, String> {

    @Query(value = "SELECT c FROM Cafe c WHERE c.address.town = :town")
    List<Cafe> findByTown(@Param(value = "town") String towns);
}
