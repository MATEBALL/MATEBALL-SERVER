package at.mateball.storage;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
public class S3Properties {

    @NotBlank
    private String bucket;

    @NotBlank
    private String defaultProfileKey;
}