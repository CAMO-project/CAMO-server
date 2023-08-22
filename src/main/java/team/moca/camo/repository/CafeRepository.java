package team.moca.camo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.domain.Cafe;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, String> {

    Page<Cafe> findDistinctByIdInAndTagsTagNameIn(List<String> cafeIdList, List<String> tagNameList, Pageable pageable);

    Page<Cafe> findByIdIn(List<String> cafeIdList, Pageable pageable);
}
