package com.afr.fms.Admin.utilis;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"

            // you can add more matching headers here ...
    };

    private HttpUtils() {
        // nothing here ...
    }

    public static String getRequestIP(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String value = request.getHeader(header);
            if (value == null || value.isEmpty()) {
                continue;
            }
            String[] parts = value.split("\\s*,\\s*");
            return parts[0];
        }
        return request.getRemoteAddr();
    }

    public static String clientIp(HttpServletRequest request) {
        String ip = HttpUtils.getRequestIP(request);
    
        if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
          try {
            return InetAddress.getLocalHost().getHostAddress();
          } catch (UnknownHostException e) {
            return null;
          }
        }
        return ip;
      }
}