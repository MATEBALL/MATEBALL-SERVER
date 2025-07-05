package at.mateball.kakao;

import at.mateball.common.jwt.JwtTokenGenerator;
import at.mateball.common.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenGeneratorTest {

    private JwtTokenGenerator jwtTokenGenerator;
    private Key key;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties(
                "test-test-test-test-test-test-test-test",
                3600000L,
                1209600000L,
                600000L
        );
        jwtTokenGenerator = new JwtTokenGenerator(props);
        key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void accessToken_생성_및_검증() {
        // given
        Long userId = 1L;

        // when
        String token = jwtTokenGenerator.generateAccessToken(userId);

        // then
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("test-test-test-test-test-test-test-test".getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    @Test
    void refreshToken_생성_성공() {
        // given
        Long userId = 123L;

        // when
        String token = jwtTokenGenerator.generateRefreshToken(userId);

        // then
        assertThat(token).isNotNull();
    }
}
