package at.mateball.common.jwt;

import at.mateball.domain.auth.api.dto.LoginResult;
import at.mateball.exception.code.BusinessErrorCode;
import at.mateball.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.Cookie;


import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtCookieProvider {
    private final JwtProperties jwtProperties;

    public JwtCookieProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private static final String ACCESS_TOKEN_NAME = "accessToken";
    private static final String REFRESH_TOKEN_NAME = "refreshToken";
    private static final String KAKAO_TOKEN_NAME = "kakaoAccessToken";

    public String extractAccessToken(HttpServletRequest request) {
        return extractCookie(request, ACCESS_TOKEN_NAME)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.INVALID_ACCESS_TOKEN));
    }

    public String extractRefreshToken(HttpServletRequest request) {
        return extractCookie(request, REFRESH_TOKEN_NAME)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.MISSING_TOKEN));
    }

    public String extractKakaoToken(HttpServletRequest request) {
        return extractCookie(request, KAKAO_TOKEN_NAME)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.MISSING_TOKEN));
    }

    public List<ResponseCookie> createAllCookies(LoginResult result) {
        return List.of(
                createCookie(ACCESS_TOKEN_NAME, result.accessToken(), jwtProperties.accessExpiration()),
                createCookie(REFRESH_TOKEN_NAME, result.refreshToken(), jwtProperties.refreshExpiration()),
                createCookie(KAKAO_TOKEN_NAME, result.kakaoAccessToken(), jwtProperties.kakaoExpiration())
        );
    }

    public List<ResponseCookie> deleteAllCookies(HttpServletRequest request) {
        return List.of(
                deleteCookie(ACCESS_TOKEN_NAME, request),
                deleteCookie(REFRESH_TOKEN_NAME, request),
                deleteCookie(KAKAO_TOKEN_NAME, request)
        );
    }

    private Optional<String> extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private ResponseCookie createCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMillis(maxAge))
                .build();
    }

    private ResponseCookie deleteCookie(String name, HttpServletRequest request) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();
    }

    private boolean isSecure(HttpServletRequest request) {
        String serverName = request.getServerName();
        return !isLocalhost(serverName);
    }

    private boolean isLocalhost(String host) {
        return "localhost".equals(host)
                || "127.0.0.1".equals(host)
                || "::1".equals(host);
    }

    private String getSameSite(HttpServletRequest request) {
        return isSecure(request) ? "None" : "Lax";
    }
}
