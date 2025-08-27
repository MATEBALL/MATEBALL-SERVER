package at.mateball.domain.auth.core.service;

import at.mateball.common.jwt.JwtCookieProvider;
import at.mateball.common.jwt.JwtTokenGenerator;
import at.mateball.domain.auth.api.dto.LoginResult;
import at.mateball.domain.user.core.User;
import at.mateball.domain.user.core.repository.UserRepository;
import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReissueService {
    private final JwtCookieProvider jwtCookieProvider;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public ReissueService(JwtCookieProvider jwtCookieProvider, JwtTokenGenerator jwtTokenGenerator, TokenService tokenService, UserRepository userRepository) {
        this.jwtCookieProvider = jwtCookieProvider;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Transactional
    public LoginResult reissue(HttpServletRequest request) {
        String refreshToken = jwtCookieProvider.extractRefreshToken(request);

        Long userId;

        try {
            userId = jwtTokenGenerator.extractUserId(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(BusinessErrorCode.EXPIRED_TOKEN);
        }

        if (!tokenService.validate(userId, refreshToken)) {
            throw new BusinessException(BusinessErrorCode.EXPIRED_TOKEN);
        }

        String newAccessToken = jwtTokenGenerator.generateAccessToken(userId);
        String newRefreshToken = jwtTokenGenerator.generateRefreshToken(userId);

        tokenService.save(userId, newRefreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.USER_NOT_FOUND));

        return new LoginResult(
                newAccessToken,
                newRefreshToken,
                null,
                user.getId(),
                user.getEmail()
        );
    }
}
