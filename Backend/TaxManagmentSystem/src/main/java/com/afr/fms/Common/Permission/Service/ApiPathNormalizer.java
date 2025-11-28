package com.afr.fms.Common.Permission.Service;

import javax.servlet.http.HttpServletRequest;

public class ApiPathNormalizer {

    public static String normalizeSpringBootPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        

        // Skip root or empty paths
        if (uri == null || uri.isEmpty() || uri.equals("/"))
            return uri;

        String[] segments = uri.split("/");

        if (segments.length <= 1)
            return uri;

        // Replace the last non-empty segment with "param"
        for (int i = segments.length - 1; i >= 0; i--) {
            if (!segments[i].isEmpty()) {
                segments[i] = "param";
                break;
            }
        }

        // Reconstruct the normalized path
        return String.join("/", segments);
    }

}
