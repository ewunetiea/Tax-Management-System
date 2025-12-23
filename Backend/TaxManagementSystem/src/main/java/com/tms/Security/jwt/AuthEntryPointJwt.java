package com.tms.Security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.tms.Admin.Entity.Setting;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Mapper.SettingMapper;
import com.tms.Admin.Mapper.UserTrackerMapper;
import com.tms.Admin.utilis.HttpUtils;
import com.tms.Security.UserDetailsServiceImpl;
import com.tms.Security.UserSecurity.entity.UserSecurity;
import com.tms.Security.UserSecurity.mapper.UserSecurityMapper;
import com.tms.Security.UserSecurity.service.UserSecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ua_parser.Client;
import ua_parser.Parser;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserTrackerMapper userTrakerMapper;

    @Autowired
    private UserSecurityMapper userSecurityMapper;

    @Autowired
    private SettingMapper settingMapper;

    private Setting setting;

    // Fast, thread-safe UA parser (cached)
    private static final Parser UA_PARSER;

    static {
        try {
            UA_PARSER = new Parser();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize UA parser", e);
        }
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        setting = settingMapper.getSetting();
        long lockTimeHours = setting.getLock_time() / 3600000L; // ms → hours

        User user = userDetailsService.getUser();
        String friendlyMessage = authException.getMessage();

        // 1. Disabled account
        if (authException instanceof org.springframework.security.authentication.DisabledException) {
            friendlyMessage = "Your account is inactive. Please contact the system administrator.";
        }

        boolean alreadyTracked = false;

        // 2. Known user - handle failed attempts, lock, expired password
        if (user != null) {
            UserSecurity us = user.getUser_security();
            if (us == null) {
                us = new UserSecurity();
                us.setUser_name(user.getEmail());
            }

            if (user.isStatus() && us.isAccountNonLocked() && !authException.getMessage().startsWith("Full")) {
                if (us.getNumber_of_attempts() < setting.getMaximum_attempt() - 1) {
                    userSecurityService.increaseFailedAttempts(us);
                } else {
                    userSecurityService.lock(us);
                    authException = new LockedException(
                        "Your account has been locked due to " + setting.getMaximum_attempt() +
                        " failed attempts. It will be unlocked after " + lockTimeHours +
                        " hour(s). Contact System Administrator."
                    );
                }
            } else if (!us.isAccountNonLocked()) {
                if (!userSecurityService.unlockWhenTimeExpired(us)) {
                    authException = new LockedException(
                        "Your account is locked. Try again later or contact System Administrator."
                    );
                }
            }

            if (!us.isCredentialsNonExpired() && !authException.getMessage().startsWith("Bad")) {
                authException = new CredentialsExpiredException("password_expired " + user.getId());
            }

            // Track failed login for known user
            trackFailedLogin(request, user.getEmail());
            alreadyTracked = true;
        } 
        // 3. Unknown username
        else if (!authException.getMessage().startsWith("Full")) {
            String username = userDetailsService.getUsername();
            trackFailedLogin(request, username != null ? username : "unknown");
            userSecurityMapper.addFailedUserName(username);
            alreadyTracked = true;
        }

        // 4. If not tracked above (fallback)
        if (!alreadyTracked && user != null && !authException.getMessage().startsWith("Full")) {
            trackFailedLogin(request, user.getEmail());
            userSecurityMapper.addFailedUserName(user.getEmail());
        }

        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

    private void trackFailedLogin(HttpServletRequest request, String username) {
        String ip = HttpUtils.clientIp(request);
        String browserInfo = parseUserAgent(request.getHeader("User-Agent"));

        userTrakerMapper.registerUnAutorizedUsers(
            username != null && !username.isBlank() ? username : "unknown",
            browserInfo,
            ip
        );
    }

    private String parseUserAgent(String userAgentHeader) {
        if (userAgentHeader == null || userAgentHeader.isBlank()) {
            return "Unknown Browser";
        }
        try {
            Client c = UA_PARSER.parse(userAgentHeader);
            String browser = c.userAgent.family +
                (c.userAgent.major != null ? " " + c.userAgent.major : "");
            String os = c.os.family +
                (c.os.major != null ? " " + c.os.major : "");
            return browser + " on " + os;
        } catch (Exception e) {
            return "Unknown Browser";
        }
    }
}