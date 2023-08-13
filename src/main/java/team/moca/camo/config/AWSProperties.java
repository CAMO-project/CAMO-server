package team.moca.camo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws")
@Component
public class AWSProperties {

    private String accessKey;
    private String secretKey;
    private String bucketName;
}
