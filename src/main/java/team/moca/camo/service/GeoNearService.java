package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import team.moca.camo.domain.GeoLocation;
import team.moca.camo.repository.CafeLocationRepository;

import java.util.List;

@Slf4j
@Service
public class GeoNearService {

    private final MongoTemplate mongoTemplate;
    private final CafeLocationRepository cafeLocationRepository;

    public GeoNearService(MongoTemplate mongoTemplate, CafeLocationRepository cafeLocationRepository) {
        this.mongoTemplate = mongoTemplate;
        this.cafeLocationRepository = cafeLocationRepository;
    }

    public void test() {
        Point location = new Point(127.47653874, 37.23846753);
        Distance distance = new Distance(1000000);

        List<GeoLocation> locations = cafeLocationRepository.findByLocationNear(location, distance);
        log.info("locations = {}", locations);
    }
}
