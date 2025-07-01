package at.mateball.domain.auth.core.config;

import at.mateball.domain.auth.api.dto.kako.KakaoTokenRes;
import at.mateball.domain.auth.api.dto.kako.KakaoUserRes;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class OauthClientApi {

    private final WebClient kakaoAuthClient;
    private final WebClient kakaoApiClient;
    private final KakaoOauthProperties properties;

    public OauthClientApi(
            @Qualifier("kakaoAuthClient") WebClient kakaoAuthClient,
            @Qualifier("kakaoApiClient") WebClient kakaoApiClient,
            KakaoOauthProperties properties    ) {
        this.kakaoAuthClient = kakaoAuthClient;
        this.kakaoApiClient = kakaoApiClient;
        this.properties = properties;
    }

    public KakaoTokenRes fetchToken(String code, String redirectUri) {
        try {
            return kakaoAuthClient.post()
                    .uri("/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", properties.clientId())
                            .with("redirect_uri", redirectUri)
                            .with("code", code)
                            .with("client_secret", properties.clientSecret()))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).flatMap(errorBody -> {
                                log.error("카카오 토큰 요청 실패 - 상태: {}, 응답: {}", response.statusCode(), errorBody);
                                return Mono.error(new BusinessException(BusinessErrorCode.KAKAO_CLIENT_ERROR));
                            })
                    )
                    .bodyToMono(KakaoTokenRes.class)
                    .block();
        } catch (Exception e) {
            log.error("카카오 토큰 요청 중 예외 발생", e);
            throw new BusinessException(BusinessErrorCode.KAKAO_CLIENT_ERROR);
        }
    }

    public KakaoUserRes fetchUser(String accessToken) {
        try {
            return kakaoApiClient.get()
                    .uri("/v2/user/me")
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).flatMap(errorBody -> {
                                log.error("카카오 사용자 정보 요청 실패 - 응답: {}", errorBody);
                                return Mono.error(new BusinessException(BusinessErrorCode.KAKAO_CLIENT_ERROR));
                            })
                    )
                    .bodyToMono(KakaoUserRes.class)
                    .block();
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 중 예외 발생", e);
            throw new BusinessException(BusinessErrorCode.KAKAO_CLIENT_ERROR);
        }
    }
}
