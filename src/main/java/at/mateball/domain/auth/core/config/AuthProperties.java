package at.mateball.domain.auth.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.redirect-uri")
public class AuthProperties {
    private String local;
    private String prod;
}
