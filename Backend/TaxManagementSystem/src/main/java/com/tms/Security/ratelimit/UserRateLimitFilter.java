package com.tms.Security.ratelimit;

import com.tms.Security.jwt.JwtUtils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    // Paths that are NOT rate-limited
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/api/auth/signin",
            "/api/auth/force-login",
            "/api/auth/refreshtoken",
            "/api/auth/signup",
            "/api/checkUserEmployeeIdSystem",
            "/api/checkUserEmployeeId",
            "/api/region/active",
            "/api/branch/active",
            "/api/selected_job_position",
            "/swagger-ui",
            "/swagger-ui/",
            "/v3/api-docs",
            "/v3/api-docs/"
    );

    // One bucket per user
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // 20 requests per minute per user
    private final Bandwidth limit = Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1)));

    private Bucket resolveBucket(String username) {
        return buckets.computeIfAbsent(username, k -> Bucket.builder()
                .addLimit(limit)
                .build());
    }

    private String extractUsername(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromCookies(request);
        if (token == null) return null;
        try {
            return jwtUtils.getUserNameFromJwtToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip non-API paths
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Skip excluded paths (including Swagger)
        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith) ||
            path.startsWith("/api/checkUserEmail") ||
            path.startsWith("/api/checkUsername") ||
            path.startsWith("/api/checkUserPhoneNumber") ||
            path.startsWith("/api/auth/signout")) {

            filterChain.doFilter(request, response);
            return;
        }

        String username = extractUsername(request);

        if (username == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized - Invalid or missing token\"}");
            return;
        }

        Bucket bucket = resolveBucket(username);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"error\": \"Too Many Requests\", \"message\": \"Rate limit exceeded (20 requests/min). Try again later.\"}"
            );
        }
    }
}