package at.mateball.config;

import at.mateball.domain.auth.core.config.AuthProperties;
import at.mateball.domain.auth.core.config.KakaoOauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        KakaoOauthProperties.class,
        AuthProperties.class
})
public class OauthConfig {

}
