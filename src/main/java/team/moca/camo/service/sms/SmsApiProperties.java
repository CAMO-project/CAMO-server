package team.moca.camo.service.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("sms")
@Component
public class SmsApiProperties {

    private String apiKey;
    private String apiSecretKey;
    private String url;
    private String outgoingNumber;
}
