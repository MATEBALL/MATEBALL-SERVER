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

        log.info("카카오 유저 ID = {}", kakaoUser.id());
        if (kakaoUser.kakaoAccount() != null) {
            log.info("카카오 이메일 = {}", kakaoUser.kakaoAccount().email());
            if (kakaoUser.kakaoAccount().profile() != null) {
                log.info("카카오 닉네임 = {}", kakaoUser.kakaoAccount().profile().nickname());
                log.info("카카오 프로필 이미지 = {}", kakaoUser.kakaoAccount().profile().profileImageUrl());
                log.info("카카오 기본 이미지 여부 = {}", kakaoUser.kakaoAccount().profile().isDefaultImage());
            }
            log.info("카카오 profile_image_needs_agreement = {}", kakaoUser.kakaoAccount().profileImageNeedsAgreement());
        }

        boolean isNewUser = !userRepository.findByKakaoUserId(kakaoUser.id()).isPresent();

        User user = userRepository.findByKakaoUserId(kakaoUser.id())
                .orElseGet(() -> {
                    User newUser = kakaoUser.toEntity();
                    if (kakaoUser.isProfileImageAgreed()) {
                        String profileImageUrl = kakaoUser.extractProfileImageUrl();
                        newUser.updateProfileImage(profileImageUrl);
                    }
                    User saved = userRepository.save(newUser);
                    log.info("신규 가입자 생성됨 : imgUrl={}", saved.getImgUrl());
                    return saved;
                });

        if (!isNewUser) {
            if (kakaoUser.isProfileImageAgreed()) {
                String profileImageUrl = kakaoUser.extractProfileImageUrl();
                if (profileImageUrl != null) {
                    user.updateProfileImage(profileImageUrl);
                }
            } else {
                user.updateProfileImage(null);
            }
        }

        String accessToken = jwtTokenGenerator.generateAccessToken(user.getId());
        String refreshToken = jwtTokenGenerator.generateRefreshToken(user.getId());

        tokenService.save(user.getId(), refreshToken);
        log.info("카카오 로그인 성공 - userId: {}", user.getId());

        return new LoginResult(
                accessToken,
                refreshToken,
                kakaoToken.accessToken(),
                user.getId(),
                user.getEmail(),
                user.getImgUrl()
        );
    }
}
