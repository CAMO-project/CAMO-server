package team.moca.camo.security;

import lombok.Getter;

import java.util.Base64;

@Getter
public class JwtTestProperties implements JwtProperties {

    private final String issuer = "test_issuer";
    private final String secretKey = "test_secret_key";

    @Override
    public byte[] getEncodedSecretKey() {
        return Base64.getEncoder().encode(secretKey.getBytes());
    }
}
