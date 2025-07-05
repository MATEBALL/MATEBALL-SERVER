package at.mateball.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {
    private static final String[] LOCAL_HOSTS = {"localhost", "127.0.0.1", "::1"};

    public static boolean isLocalhost(String host) {
        if (host == null) {
            return false;
        }

        for (String local : LOCAL_HOSTS) {
            if(local.equals(host)) return true;
        }

        return false;
    }

    public static boolean isLocalRequest(HttpServletRequest request) {
        return isLocalhost(request.getServerName());
    }

    public static boolean isLocalOrigin(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        String base = origin != null ? origin : referer;

        if (base == null) {
            return false;
        }

        for (String local : LOCAL_HOSTS) {
            if(base.contains(local)) return true;
        }

        return false;
    }
}
