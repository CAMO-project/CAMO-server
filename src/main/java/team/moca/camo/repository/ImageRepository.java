package team.moca.camo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Image;

public interface ImageRepository extends JpaRepository<Image, String> {
}
