package team.moca.camo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import team.moca.camo.domain.Location;

public interface CafeLocationRepository extends MongoRepository<Location, String> {

    Page<Location> findByCoordinatesNear(Point coordinates, Distance maxDistance, Pageable pageable);
}
