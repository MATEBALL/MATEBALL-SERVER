package at.mateball.common.jwt;

import at.mateball.exception.BusinessException;
import at.mateball.exception.code.BusinessErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtCookieProvider cookieProvider;
    private final JwtTokenValidator tokenValidator;
    private final JwtAuthenticationProvider authenticationProvider;

    public JwtAuthenticationFilter(JwtCookieProvider cookieProvider,
                                   JwtTokenValidator tokenValidator,
                                   JwtAuthenticationProvider authenticationProvider) {
        this.cookieProvider = cookieProvider;
        this.tokenValidator = tokenValidator;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = cookieProvider.extractAccessToken(request);

            if (token != null && token.contains(".") && countDots(token) == 2) {
                tokenValidator.validate(token);
                Authentication authentication = authenticationProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException e) {
            throw new BusinessException(BusinessErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new BusinessException(BusinessErrorCode.INVALID_SERVER_JWT);
        }

        filterChain.doFilter(request, response);
    }

    private int countDots(String token) {
        return (int) token.chars().filter(ch -> ch == '.').count();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/");
    }
}
