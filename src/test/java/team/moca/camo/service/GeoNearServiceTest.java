package team.moca.camo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeoNearServiceTest {

    @Autowired
    GeoNearService geoNearService;

    @Test
    void test() {
        geoNearService.test();
    }
}