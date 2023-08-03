package team.moca.camo.security.jwt;

public interface JwtProperties {

    String getIssuer();

    byte[] getEncodedSecretKey();
}
