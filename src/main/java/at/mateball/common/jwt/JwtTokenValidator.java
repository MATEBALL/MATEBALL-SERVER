package at.mateball.common.jwt;

import at.mateball.exception.BusinessErrorCode;
import at.mateball.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
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
            throw new BusinessException(BusinessErrorCode.KAKAO_CLIENT_ERROR);
        }
    }
}
