package team.moca.camo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import team.moca.camo.domain.Location;

import java.util.List;

public interface CafeLocationRepository extends MongoRepository<Location, String> {

    Page<Location> findByCoordinatesNear(Point point, Distance distance, Pageable pageable);

    List<Location> findByCoordinatesNear(Point point, Distance distance);

    Page<Location> findByIdInAndCoordinatesNear(List<String> idList, Point coordinates, Distance maxDistance, Pageable pageable);
}
