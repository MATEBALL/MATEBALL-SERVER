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
}

