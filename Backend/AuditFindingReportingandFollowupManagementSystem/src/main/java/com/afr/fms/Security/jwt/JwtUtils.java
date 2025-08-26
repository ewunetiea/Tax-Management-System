package com.afr.fms.Security.jwt;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.afr.fms.Admin.Entity.Setting;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Service.SettingService;
import com.afr.fms.Security.UserDetailsImpl;
import com.afr.fms.Security.UserSecurity.entity.UserSecurity;
import com.afr.fms.Security.UserSecurity.service.UserSecurityService;

import io.jsonwebtoken.*;

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

  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal, HttpServletRequest request) {
    resetFailedAttempts(userPrincipal.getUserSecurity());
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    String contextPath = getContextPath(request);
    return generateCookie(jwtCookie, jwt, contextPath + "/api");
  }

  public void resetFailedAttempts(UserSecurity userSecurity) {
    if (userSecurity.getNumber_of_attempts() > 0) {
      userSecurityService.resetFailedAttempts(userSecurity);
    }
  }

  public ResponseCookie generateJwtCookie(User user, HttpServletRequest request) {
    String jwt = generateTokenFromUsername(user.getUsername());
    String contextPath = getContextPath(request);
    return generateCookie(jwtCookie, jwt, contextPath + "/api");

  }

  public ResponseCookie generateRefreshJwtCookie(String refreshToken, HttpServletRequest request) {
    String contextPath = getContextPath(request);
    return generateCookie(jwtRefreshCookie, refreshToken, contextPath + "/api/auth/refreshtoken");
  }

  public String getJwtFromCookies(HttpServletRequest request) {
    String contextPath = getContextPath(request);

    return getCookieValueByName(request, jwtCookie);
  }

  public String getJwtRefreshFromCookies(HttpServletRequest request) {
    return getCookieValueByName(request, jwtRefreshCookie);
  }

  public ResponseCookie getCleanJwtCookie(HttpServletRequest request) {
    String contextPath = getContextPath(request);
    return generateCleanCookie(jwtCookie, contextPath + "/api");
  }

  public ResponseCookie getCleanJwtRefreshCookie(HttpServletRequest request) {
    String contextPath = getContextPath(request);
    return generateCleanCookie(jwtRefreshCookie, contextPath + "/api/auth/refreshtoken");
  }

  private String getContextPath(HttpServletRequest request) {
    return request.getContextPath();
  }

  private ResponseCookie generateCleanCookie(String name, String path) {
    return ResponseCookie.from(name, null).path("/").maxAge(0).httpOnly(true).secure(true).build();
  }

  public String getUserNameFromJwtToken(String token) {
    setting = settingService.getSetting();
    return Jwts.parser().setSigningKey(setting.getJwt_secret()).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      setting = settingService.getSetting();
      Jwts.parser().setSigningKey(setting.getJwt_secret()).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
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
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + setting.getJwt_expiration()))
        .signWith(SignatureAlgorithm.HS512, setting.getJwt_secret())
        .compact();
  }

  private ResponseCookie generateCookie(String name, String value, String path) {
    return ResponseCookie.from(name, value)
        .path("/")
        .maxAge(24 * 60 * 60)
        .httpOnly(true)
        .secure(true)
        .build();
  }

  private String getCookieValueByName(HttpServletRequest request, String name) {
    Cookie cookie = WebUtils.getCookie(request, name);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }
}