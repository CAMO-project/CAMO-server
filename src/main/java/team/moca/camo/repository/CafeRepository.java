package team.moca.camo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.moca.camo.domain.Cafe;

public interface CafeRepository extends JpaRepository<Cafe, String> {

    @Query(value = "SELECT c FROM Cafe c WHERE c.address.city = :city")
    Page<Cafe> findByCity(@Param(value = "city") String city, Pageable pageable);
}
