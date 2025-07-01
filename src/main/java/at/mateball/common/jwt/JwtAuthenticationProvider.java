package at.mateball.common.jwt;

import at.mateball.exception.BusinessErrorCode;
import at.mateball.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtAuthenticationProvider {
    private final JwtProperties jwtProperties;
    private final Key key;

    public JwtAuthenticationProvider(JwtProperties jwtProperties, Key key) {
        this.jwtProperties = jwtProperties;
        this.key = key;
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
}
