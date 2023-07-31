package team.moca.camo.security;

public interface JwtProperties {

    String getIssuer();

    String getSecretKey();

    byte[] getEncodedSecretKey();
}
