package at.mateball.domain.auth.api.controller;

import at.mateball.common.MateballResponse;
import at.mateball.common.jwt.JwtCookieProvider;
import at.mateball.common.swagger.CustomExceptionDescription;
import at.mateball.domain.auth.api.dto.LoginCommand;
import at.mateball.domain.auth.core.service.LoginService;
import at.mateball.domain.auth.api.dto.LoginResult;
import at.mateball.domain.auth.api.dto.LoginUserInfo;
import at.mateball.exception.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import at.mateball.common.swagger.SwaggerResponseDescription;

import java.util.List;

@RestController
public class OAuthController {
    private final JwtCookieProvider jwtCookieProvider;
    private final LoginService loginService;

    public OAuthController(JwtCookieProvider jwtCookieProvider, LoginService loginService) {
        this.jwtCookieProvider = jwtCookieProvider;
        this.loginService = loginService;
    }

    @CustomExceptionDescription(SwaggerResponseDescription.POST_KAKAO_LOGIN)
    @Operation(summary = "카카오 소셜 로그인")
    @PostMapping("/auth/login")
    public ResponseEntity<MateballResponse<?>> login(@RequestBody LoginCommand loginCommand) {
        LoginResult result = loginService.login(loginCommand);
        List<ResponseCookie> cookies = jwtCookieProvider.createAllCookies(result);

        LoginUserInfo userInfo = new LoginUserInfo(
                result.userId(),
                result.email()
        );

        return withCookies(cookies).body(MateballResponse.success(SuccessCode.OK, userInfo));
    }

    private ResponseEntity.BodyBuilder withCookies(List<ResponseCookie> cookies) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        for (ResponseCookie cookie : cookies) {
            builder.header(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return builder;
    }
}
