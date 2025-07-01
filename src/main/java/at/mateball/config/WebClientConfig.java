package at.mateball.config;

import at.mateball.domain.auth.core.config.KakaoOauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(KakaoOauthProperties.class)
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader("User-Agent", "mateball-client");
    }

    @Bean
    public WebClient kakaoAuthClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://kauth.kakao.com")
                .build();
    }

    @Bean
    public WebClient kakaoApiClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://kapi.kakao.com")
                .build();
    }
}