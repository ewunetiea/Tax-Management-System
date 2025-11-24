package com.afr.fms.Security.ratelimit;

import com.afr.fms.Security.jwt.JwtUtils;
import io.github.bucket4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserRateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    // ---- EXCLUDED ENDPOINTS ----
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/api/auth/signin",
            "/api/auth/force-login",
            "/api/auth/refreshtoken",
            "/api/auth/signup"
    );

    // User buckets
    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.simple(20, Duration.ofMinutes(1)); //10 req per min
        return Bucket4j.builder().addLimit(limit).build();
    }

    private Bucket resolveBucket(String key) {
        return userBuckets.computeIfAbsent(key, k -> createBucket());
    }

    private String extractUsername(HttpServletRequest req) {
        String token = jwtUtils.getJwtFromCookies(req);
        if (token == null) return null;
        try {
            return jwtUtils.getUserNameFromJwtToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        System.out.println("Pathhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh:" + path);
        // Only filter /api/*
        if (!path.startsWith("/api/")) {
            chain.doFilter(req, res);
            return;
        }

        // ---- EXCLUDE STATIC PATHS ----
        if (EXCLUDED_PATHS.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        // ---- EXCLUDE DYNAMIC SIGNOUT PATH ----
        if (path.startsWith("/api/auth/signout")) {
            chain.doFilter(req, res);
            return;
        }

        // ---- RATE LIMITING ----
        String username = extractUsername(req);
        if (username == null) {
            res.setStatus(401);
            res.getWriter().write("Unauthorized - Missing or invalid JWT");
            return;
        }

        Bucket bucket = resolveBucket(username);

        if (bucket.tryConsume(1)) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(429);
            res.getWriter().write("Rate limit exceeded (10 requests/min) for user: " + username);
        }
    }
}
