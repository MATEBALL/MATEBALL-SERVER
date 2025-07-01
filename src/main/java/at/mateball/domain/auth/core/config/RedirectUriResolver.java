package at.mateball.domain.auth.core.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectUriResolver {

    private final AuthProperties authProperties;
    private static final String LOCAL_IDENTIFIER = "localhost";

    public String resolve(HttpServletRequest request) {
        return isLocalRequest(request) ? authProperties.getLocal() : authProperties.getProd();
    }

    private boolean isLocalRequest(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        String base = origin != null ? origin : referer;

        return base != null && base.contains(LOCAL_IDENTIFIER);
    }
}
