package at.mateball.common.jwt;

import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.exception.BusinessException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@Slf4j
public class JwtTokenValidator {

    private final JwtProperties jwtProperties;
    private final Key key;

    public JwtTokenValidator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public void validate(String token) {
        try {
            if (!token.contains(".")) {
                throw new BusinessException(BusinessErrorCode.INVALID_SERVER_JWT);
            }

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT validation failed: {}", e.getMessage());
            throw new BusinessException(BusinessErrorCode.INVALID_SERVER_JWT);
        }
    }
}
