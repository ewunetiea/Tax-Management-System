package com.tms.Security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.tms.Admin.Entity.Setting;
import com.tms.Admin.Service.SettingService;
import com.tms.Security.UserDetailsImpl;
import com.tms.Security.UserSecurity.entity.UserSecurity;
import com.tms.Security.UserSecurity.service.UserSecurityService;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private SettingService settingService;

    private Setting setting;

    @Value("${dreameraba.app.jwtCookieName}")
    private String jwtCookie;

    @Value("${dreameraba.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    // Cache the parser to avoid recreating it every time
    private JwtParser jwtParser;

    private JwtParser getJwtParser() {
        if (jwtParser == null) {
            setting = settingService.getSetting();
            byte[] keyBytes = setting.getJwt_secret().getBytes();
            jwtParser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(keyBytes))
                    .build();
        }
        return jwtParser;
    }


    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal, HttpServletRequest request) {
        resetFailedAttempts(userPrincipal.getUserSecurity());
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return generateCookie(jwtCookie, jwt, "/");
    }

    public ResponseCookie generateJwtCookie(com.tms.Admin.Entity.User user, HttpServletRequest request) {
        String jwt = generateTokenFromUsername(user.getUsername());
        return generateCookie(jwtCookie, jwt, "/");
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken, HttpServletRequest request) {
        return generateCookie(jwtRefreshCookie, refreshToken, "/api/auth/refreshtoken");
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookie);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // ← Keep "None" for cross-site (194 → 195)
                .build();
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(jwtRefreshCookie, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // ← Keep "None" for cross-site (194 → 195)
                .build();
    }

    public String getUserNameFromJwtToken(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            getJwtParser().parseSignedClaims(authToken);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateTokenFromUsername(String username) {
        setting = settingService.getSetting();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + setting.getJwt_expiration());

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(Keys.hmacShaKeyFor(setting.getJwt_secret().getBytes()))
                .compact();
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // ← Keep "None" for cross-site (194 → 195)
                .build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    public void resetFailedAttempts(UserSecurity userSecurity) {
        if (userSecurity != null && userSecurity.getNumber_of_attempts() > 0) {
            userSecurityService.resetFailedAttempts(userSecurity);
        }
    }

    // In JwtUtils.java
    public ResponseCookie getCleanJwtCookie(HttpServletRequest request) {
        return ResponseCookie.from(jwtCookie, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // ← Keep "None" for cross-site (194 → 195)
                .build();
    }

    public ResponseCookie getCleanJwtRefreshCookie(HttpServletRequest request) {
        return ResponseCookie.from(jwtRefreshCookie, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // ← Keep "None" for cross-site (194 → 195)
                .build();
    }
}