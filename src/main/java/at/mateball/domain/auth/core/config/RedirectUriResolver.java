package at.mateball.domain.auth.core.config;

import at.mateball.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectUriResolver {

    private static final String LOCAL_IDENTIFIER = "localhost";
    private static final String LOCAL_REDIRECT_URI = "http://localhost:5173/auth";
    private static final String PROD_REDIRECT_URI = "https://mateball.co.kr/auth";

    public String resolve(HttpServletRequest request) {
        return isLocalRequest(request) ? LOCAL_REDIRECT_URI : PROD_REDIRECT_URI;
    }

    public boolean isLocalRequest(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        String base = origin != null ? origin : referer;

        return base != null && base.contains(LOCAL_IDENTIFIER);
    }
}
