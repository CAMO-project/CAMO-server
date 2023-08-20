package team.moca.camo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, String> {

    Page<Menu> findByCafeAndIsSignature(Cafe cafe, boolean isSignature, Pageable pageable);
}
