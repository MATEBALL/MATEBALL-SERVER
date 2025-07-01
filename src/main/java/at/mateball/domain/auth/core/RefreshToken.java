package at.mateball.domain.auth.core;

import jakarta.persistence.Column;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken {
    @Id
    private String key;

    @Column
    private String value;

    protected RefreshToken() {

    }

    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static RefreshToken of(Long userId, String value) {
        return new RefreshToken("RT:" + userId, value);
    }
}
