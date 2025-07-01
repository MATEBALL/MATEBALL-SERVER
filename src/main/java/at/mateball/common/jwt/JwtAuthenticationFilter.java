package at.mateball.common.jwt;

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

    public JwtAuthenticationFilter(JwtCookieProvider cookieProvider, JwtTokenValidator tokenValidator, JwtAuthenticationProvider authenticationProvider) {
        this.cookieProvider = cookieProvider;
        this.tokenValidator = tokenValidator;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = cookieProvider.extractAccessToken(request);
            tokenValidator.validate(token);
            Authentication authentication = authenticationProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.debug("JWT 인증 실패", e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/");
    }
}
