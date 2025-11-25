package com.afr.fms.Common.Permission.Service;

import javax.servlet.http.HttpServletRequest;

public class ApiPathNormalizer {

    public static String normalizeSpringBootPath(HttpServletRequest request) {

        // Get the raw URI → "/tmsbackend/api/.../1"
        String uri = request.getRequestURI();

        // Remove Tomcat context path → "/tmsbackend"
        String contextPath = request.getContextPath(); // "/tmsbackend"

        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length()); 
            // Now → "/api/admin/dashboard/recent-activity/1"
        }

        if (uri.equals("/") || uri.isEmpty()) {
            return uri;
        }

        // Split segments
        String[] segments = uri.split("/");

        // Replace ONLY last numeric segment / ID
        for (int i = segments.length - 1; i >= 0; i--) {
            if (!segments[i].isEmpty()) {

                // If last segment is a number → replace with 'param'
                if (segments[i].matches("\\d+")) {
                    segments[i] = "param";
                }

                break;
            }
        }

        // Join again
        return String.join("/", segments);
    }
}
