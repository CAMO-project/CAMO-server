package team.moca.camo.security;

public interface JwtProperties {

    String getIssuer();

    byte[] getEncodedSecretKey();
}
