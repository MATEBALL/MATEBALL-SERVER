package at.mateball.domain.auth.core.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "auth.redirect-uri")
public class AuthProperties {
    private String local;
    private String prod;
}
