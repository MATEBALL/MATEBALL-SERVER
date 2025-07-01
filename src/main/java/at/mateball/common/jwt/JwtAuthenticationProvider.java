package at.mateball.common.jwt;

import at.mateball.common.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

@Component
public class JwtAuthenticationProvider {
    private final JwtTokenGenerator jwtTokenGenerator;

    public JwtAuthenticationProvider(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public Authentication getAuthentication(String token) {
        Long userId = jwtTokenGenerator.extractUserId(token);
        CustomUserDetails userDetails = new CustomUserDetails(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
