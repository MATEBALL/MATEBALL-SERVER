package at.mateball.domain.auth.core.config;

import at.mateball.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectUriResolver {

    private final AuthProperties authProperties;

    public String resolve(HttpServletRequest request) {
        return RequestUtils.isLocalOrigin(request)
                ? authProperties.getLocal()
                : authProperties.getProd();
    }
}
