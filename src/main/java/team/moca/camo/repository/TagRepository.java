package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {
}
