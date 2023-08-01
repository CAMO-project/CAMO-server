package team.moca.camo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CamoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamoApplication.class, args);
    }

}