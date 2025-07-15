package at.mateball.domain.auth.core.service;

import at.mateball.common.jwt.JwtTokenGenerator;
import at.mateball.domain.auth.api.dto.LoginCommand;
import at.mateball.domain.auth.api.dto.LoginResult;
import at.mateball.domain.auth.api.dto.kakao.KakaoTokenRes;
import at.mateball.domain.auth.api.dto.kakao.KakaoUserRes;
import at.mateball.domain.auth.core.config.OauthClientApi;
import at.mateball.domain.auth.core.config.RedirectUriResolver;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final OauthClientApi oauthClientApi;
    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final TokenService tokenService;
    private final RedirectUriResolver redirectUriResolver;

    @Transactional
    public LoginResult login(LoginCommand loginCommand) {
        String redirectUri = "https://www.mateball.co.kr/auth";

        KakaoTokenRes kakaoToken = oauthClientApi.fetchToken(loginCommand.code(), redirectUri);

        if (kakaoToken == null || kakaoToken.accessToken() == null) {
            throw new BusinessException(BusinessErrorCode.KAKAO_TOKEN_FETCH_FAILED);
        }

        KakaoUserRes kakaoUser = oauthClientApi.fetchUser(kakaoToken.accessToken());
        if (kakaoUser == null || kakaoUser.id() == null) {
            throw new BusinessException(BusinessErrorCode.KAKAO_USER_INFO_FETCH_FAILED);
        }

        User user = userRepository.findByKakaoUserId(kakaoUser.id())
                .orElseGet(() -> userRepository.save(kakaoUser.toEntity()));

        String accessToken = jwtTokenGenerator.generateAccessToken(user.getId());
        String refreshToken = jwtTokenGenerator.generateRefreshToken(user.getId());

        tokenService.save(user.getId(), refreshToken);
        log.info("카카오 로그인 성공 - userId: {}", user.getId());

        return new LoginResult(
                accessToken,
                refreshToken,
                kakaoToken.accessToken(),
                user.getId(),
                user.getEmail()
        );
    }
}
