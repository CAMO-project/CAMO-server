package team.moca.camo.repository;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import team.moca.camo.domain.Location;

import java.util.List;

public interface CafeLocationRepository extends MongoRepository<Location, String> {

    List<Location> findByCoordinatesNear(Point coordinates, Distance maxDistance);
}
