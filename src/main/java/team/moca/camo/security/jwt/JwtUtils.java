package team.moca.camo.security.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import team.moca.camo.domain.User;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils implements Serializable {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    public JwtUtils(JwtProperties jwtProperties, UserDetailsService userDetailsService) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(User user, Duration validityDuration) {
        Date now = new Date();
        return createToken(new Date(now.getTime() + validityDuration.toMillis()), user);
    }

    private String createToken(Date expiration, User user) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer()) //발급 정보
                .setIssuedAt(new Date()) //토큰 생성 시간
                .setExpiration(expiration) //토큰 만료 시간
                .setSubject(user.getEmail()) //토큰 주제 = 사용자 이메일
                .claim("id", user.getId()) //사용자 id를 클레임으로 추가
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getEncodedSecretKey())
                .compact(); //JWT 형태로 토큰 생성
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
        User user = (User) userDetailsService.loadUserByUsername(getUsernameByToken(token));
        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }

    private String getUsernameByToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getEncodedSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractAccountIdFromToken(String token) {
        return String.valueOf(Jwts.parser()
                .setSigningKey(jwtProperties.getEncodedSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id"));
    }
}
