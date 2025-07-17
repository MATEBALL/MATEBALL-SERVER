package at.mateball.domain.auth.core.service;

import at.mateball.common.jwt.JwtTokenGenerator;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
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

    public void logout(String accessToken) {
        Long userId;

        try {
            userId = jwtTokenGenerator.extractUserId(accessToken);
        } catch (Exception exception) {
            log.warn("잘못된 AccessToken - 사용자 ID 추출 실패: {}", exception.getMessage());
            throw new BusinessException(BusinessErrorCode.INVALID_SERVER_JWT);
        }

        tokenService.delete(userId);

        try {
            Long expiration = jwtTokenGenerator.getRemainingExpiration(accessToken);
            tokenService.addToBlacklist(accessToken, expiration);
        } catch (Exception exception) {
            log.warn("AccessToken 만료 시간 계산 실패: {}", exception.getMessage());
            throw new BusinessException(BusinessErrorCode.INVALID_SERVER_JWT);
        }
    }
}
