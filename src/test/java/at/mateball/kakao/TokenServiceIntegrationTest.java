package at.mateball.kakao;

import at.mateball.domain.auth.core.RefreshToken;
import at.mateball.domain.auth.core.repository.RefreshTokenRepository;
import at.mateball.domain.auth.core.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TokenServiceIntegrationTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void 실제_redis에_refreshToken_저장확인() {
        // given
        Long userId = 123L;
        String token = "realToken";

        // when
        tokenService.save(userId, token);

        // then
        Optional<RefreshToken> saved = refreshTokenRepository.findById("RT:" + userId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getValue()).isEqualTo(token);
    }

    @Test
    void redis에_저장된_refreshToken_검증_성공() {
        // given
        Long userId = 456L;
        String token = "validateToken";
        tokenService.save(userId, token);

        // when
        boolean isValid = tokenService.validate(userId, token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    void redis에_저장된_refreshToken_검증_실패() {
        // given
        Long userId = 789L;
        String savedToken = "actualToken";
        String wrongToken = "fakeToken";
        tokenService.save(userId, savedToken);

        // when
        boolean isValid = tokenService.validate(userId, wrongToken);

        // then
        assertThat(isValid).isFalse();
    }
}

