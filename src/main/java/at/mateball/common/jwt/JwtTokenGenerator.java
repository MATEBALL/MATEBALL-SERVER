package at.mateball.common.jwt;

import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenGenerator {

    private final JwtProperties jwtProperties;
    private final Key key;

    public JwtTokenGenerator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId) {
        return generateToken(userId, jwtProperties.accessExpiration());
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(userId, jwtProperties.refreshExpiration());
    }

    private String generateToken(Long userId, long expiryMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiryMillis);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new BusinessException(BusinessErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    public Long getRemainingExpiration(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            Date expiration = claims.getExpiration();
            Long now = System.currentTimeMillis();

            return expiration.getTime() - now;
        } catch (Exception exception) {
            throw new BusinessException(BusinessErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
}
