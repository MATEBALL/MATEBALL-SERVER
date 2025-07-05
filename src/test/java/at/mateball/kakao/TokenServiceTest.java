package at.mateball.kakao;

import at.mateball.domain.auth.core.RefreshToken;
import at.mateball.domain.auth.core.repository.RefreshTokenRepository;
import at.mateball.domain.auth.core.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void save_호출시_리포지토리에_저장됨() {
        Long userId = 1L;
        String token = "testToken";

        tokenService.save(userId, token);

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void validate_정상토큰_일치하면_true() {
        Long userId = 1L;
        String token = "testToken";

        when(refreshTokenRepository.findById("RT:" + userId))
                .thenReturn(Optional.of(new RefreshToken("RT:" + userId, token)));

        boolean result = tokenService.validate(userId, token);

        assertThat(result).isTrue();
    }
}
