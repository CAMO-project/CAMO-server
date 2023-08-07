package team.moca.camo.repository;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import team.moca.camo.domain.GeoLocation;

import java.util.List;

public interface CafeLocationRepository extends MongoRepository<GeoLocation, String> {

    List<GeoLocation> findByLocationNear(Point point, Distance distance);
}
