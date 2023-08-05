package team.moca.camo.security.mock;

import lombok.Getter;
import team.moca.camo.security.jwt.JwtProperties;

import java.util.Base64;

@Getter
public class MockJwtProperties implements JwtProperties {

    private final String issuer = "test_issuer";
    private final String secretKey = "test_secret_key";

    @Override
    public byte[] getEncodedSecretKey() {
        return Base64.getEncoder().encode(secretKey.getBytes());
    }
}
