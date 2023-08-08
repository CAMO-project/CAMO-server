package team.moca.camo.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("kakao")
@Component
public class KakaoProperties {

    private String apiKey;
}
