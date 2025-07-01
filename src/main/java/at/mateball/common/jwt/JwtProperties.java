package at.mateball.common.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Long accessExpiration,
        Long refreshExpiration,
        Long kakaoExpiration
) {
}
