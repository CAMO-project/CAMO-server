package team.moca.camo.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Getter
@Setter
@ConfigurationProperties("jwt")
@Component
public class JwtSpringProperties implements JwtProperties {

    private String issuer;
    private String secretKey;

    public byte[] getEncodedSecretKey() {
        return Base64.getEncoder().encode(secretKey.getBytes());
    }
}
