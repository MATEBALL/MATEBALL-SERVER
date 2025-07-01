package at.mateball.config;

import at.mateball.domain.KakaoOauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KakaoOauthProperties.class)
public class OauthConfig {
}
