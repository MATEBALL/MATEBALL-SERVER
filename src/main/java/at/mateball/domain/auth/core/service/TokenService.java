package at.mateball.domain.auth.core.service;

import at.mateball.domain.auth.core.RefreshToken;
import at.mateball.domain.auth.core.repository.RefreshTokenRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenService(RefreshTokenRepository refreshTokenRepository, RedisTemplate<String, String> redisTemplate) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisTemplate = redisTemplate;
    }

    public void save(Long userId, String refreshToken) {
        refreshTokenRepository.save(RefreshToken.of(userId, refreshToken));
    }

    public boolean validate(Long userId, String refreshToken) {
        return refreshTokenRepository.findById("RT:" + userId)
                .map(stored -> stored.getValue().equals(refreshToken))
                .orElse(false);
    }

    public void delete(Long userId) {
        refreshTokenRepository.deleteById("RT:" + userId);
    }

    public void addToBlacklist(String accessToken, Long expirationMillis) {
        redisTemplate.opsForValue().set("BL:" + accessToken, "logout", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("BL:" + accessToken));
    }
}
