package at.mateball.domain;

import at.mateball.common.MateballResponse;
import at.mateball.common.jwt.JwtCookieProvider;
import at.mateball.exception.code.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OAuthController {
    private final JwtCookieProvider jwtCookieProvider;
    private final LoginService loginService;

    public OAuthController(JwtCookieProvider jwtCookieProvider, LoginService loginService) {
        this.jwtCookieProvider = jwtCookieProvider;
        this.loginService = loginService;
    }

    @GetMapping("/auth/login")
    public ResponseEntity<MateballResponse<?>> login(@RequestParam String code, HttpServletRequest request) {
        LoginResult result = loginService.login(new LoginCommand(code), request);
        List<ResponseCookie> cookies = jwtCookieProvider.createAllCookies(result, request);

        LoginUserInfo userInfo = new LoginUserInfo(result.userId(), result.gender(), result.birthyear());

        return withCookies(cookies).body(MateballResponse.success(SuccessCode.OK, userInfo));
    }

    private ResponseEntity.BodyBuilder withCookies(List<ResponseCookie> cookies) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        for (ResponseCookie cookie : cookies) {
            builder.header(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return builder;
    }

    private <T> ResponseEntity<MateballResponse<T>> okWithCookies(SuccessCode code, T data, List<ResponseCookie> cookies) {
        return withCookies(cookies).body(MateballResponse.success(code, data));
    }
}
