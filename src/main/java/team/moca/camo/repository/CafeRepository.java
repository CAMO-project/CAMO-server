package team.moca.camo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;
import team.moca.camo.repository.projection.CafeIdProjection;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, String> {

    List<CafeIdProjection> findByTagsIdIn(List<String> tagIdList);

    Page<Cafe> findDistinctByIdInAndTagsIdIn(List<String> idList, List<String> tagIdList, Pageable pageable);

    Page<Cafe> findByIdIn(List<String> idList, Pageable pageable);
}
