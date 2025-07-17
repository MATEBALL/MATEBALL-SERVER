package at.mateball.domain.auth.core.service;

import at.mateball.common.jwt.JwtTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogoutService {
    private final TokenService tokenService;
    private final JwtTokenGenerator jwtTokenGenerator;

    public LogoutService(TokenService tokenService, JwtTokenGenerator jwtTokenGenerator) {
        this.tokenService = tokenService;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }
}
