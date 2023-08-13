package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByCafe(Cafe cafe);
}
