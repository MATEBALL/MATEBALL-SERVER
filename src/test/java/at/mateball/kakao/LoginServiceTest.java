package at.mateball.kakao;

import at.mateball.common.jwt.JwtTokenGenerator;
import at.mateball.domain.auth.api.dto.LoginCommand;
import at.mateball.domain.auth.api.dto.LoginResult;
import at.mateball.domain.auth.api.dto.kakao.KakaoTokenRes;
import at.mateball.domain.auth.api.dto.kakao.KakaoUserRes;
import at.mateball.domain.auth.api.dto.kakao.KakaoAccount;
import at.mateball.domain.auth.core.config.OauthClientApi;
import at.mateball.domain.auth.core.config.RedirectUriResolver;
import at.mateball.domain.auth.core.service.LoginService;
import at.mateball.domain.auth.core.service.TokenService;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private OauthClientApi oauthClientApi;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    @Mock
    private TokenService tokenService;

    @Mock
    private RedirectUriResolver redirectUriResolver;

    @InjectMocks
    private LoginService loginService;

    @Mock
    private HttpServletRequest request;

    @Test
    void login_신규유저_정상로그인() {
        // given
        String code = "auth_code";
        String redirectUri = "https://mateball.co.kr/auth";
        String kakaoAccessToken = "kakaoAccessToken";
        String kakaoRefreshToken = "kakaoRefreshToken";

        Long kakaoUserId = 12345L;
        String gender = "male";
        String birthYear = "1994";

        KakaoTokenRes kakaoTokenRes = new KakaoTokenRes(kakaoAccessToken, kakaoRefreshToken);
        KakaoUserRes kakaoUserRes = new KakaoUserRes(kakaoUserId,
                new KakaoAccount(gender, birthYear));
        User userEntity = kakaoUserRes.toEntity();
        ReflectionTestUtils.setField(userEntity, "id", 1L);

        String jwtAccessToken = "jwtAccessToken";
        String jwtRefreshToken = "jwtRefreshToken";
        LoginCommand command = new LoginCommand(code);

        // when
        when(redirectUriResolver.resolve(request)).thenReturn(redirectUri);
        when(oauthClientApi.fetchToken(code, redirectUri)).thenReturn(kakaoTokenRes);
        when(oauthClientApi.fetchUser(kakaoAccessToken)).thenReturn(kakaoUserRes);
        when(userRepository.findByKakaoUserId(kakaoUserId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(jwtTokenGenerator.generateAccessToken(1L)).thenReturn(jwtAccessToken);
        when(jwtTokenGenerator.generateRefreshToken(1L)).thenReturn(jwtRefreshToken);

        LoginResult result = loginService.login(command, request);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getGender()).isEqualTo(gender);
        assertThat(savedUser.getBirthYear()).isEqualTo(birthYear);

        assertThat(result.accessToken()).isEqualTo(jwtAccessToken);
        assertThat(result.refreshToken()).isEqualTo(jwtRefreshToken);
        assertThat(result.kakaoAccessToken()).isEqualTo(kakaoAccessToken);
        assertThat(result.userId()).isEqualTo(1L);

        verify(oauthClientApi).fetchToken(code, redirectUri);
        verify(oauthClientApi).fetchUser(kakaoAccessToken);
        verify(userRepository).save(any(User.class));
        verify(jwtTokenGenerator).generateAccessToken(1L);
        verify(jwtTokenGenerator).generateRefreshToken(1L);
        verify(tokenService).save(1L, jwtRefreshToken);
    }
}
