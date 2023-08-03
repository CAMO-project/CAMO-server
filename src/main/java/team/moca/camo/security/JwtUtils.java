package team.moca.camo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import team.moca.camo.domain.User;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Slf4j
@Component
public class JwtUtils implements Serializable {

    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(User user, Duration validityDuration) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + validityDuration.toMillis()), user);
    }

    private String makeToken(Date expiration, User user) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getEncodedSecretKey())
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getEncodedSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Error: ", e);
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        org.springframework.security.core.userdetails.User user =
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getEncodedSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
