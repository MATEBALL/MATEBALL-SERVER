package at.mateball.domain.auth.core.service;

import at.mateball.domain.auth.core.RefreshToken;
import at.mateball.domain.auth.core.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
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
}
