package at.mateball.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.client")
public record KakaoOauthProperties(String clientId, String clientSecret, String redirectUri) {
}
