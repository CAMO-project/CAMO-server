package team.moca.camo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, String> {

    Page<Cafe> findDistinctByIdInAndTagsIdIn(List<String> idList, List<String> tagIdList, Pageable pageable);

    Page<Cafe> findByIdIn(List<String> idList, Pageable pageable);
}
