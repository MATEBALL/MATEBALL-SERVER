package at.mateball.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static boolean isLocalOrigin(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String host = request.getServerName();
        return (origin != null && origin.contains("localhost"))
                || "localhost".equals(host)
                || "127.0.0.1".equals(host)
                || "::1".equals(host);
    }
}
