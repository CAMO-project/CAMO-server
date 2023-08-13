package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, String> {

    List<Menu> findByCafeAndSignature(Cafe cafe, boolean isSignature);
}
