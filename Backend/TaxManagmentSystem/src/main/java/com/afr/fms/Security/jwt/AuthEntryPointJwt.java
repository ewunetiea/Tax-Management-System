package com.afr.fms.Security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.afr.fms.Admin.Entity.Setting;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.SettingMapper;
import com.afr.fms.Admin.Mapper.UserTrackerMapper;
import com.afr.fms.Admin.utilis.HttpUtils;
import com.afr.fms.Security.UserDetailsServiceImpl;
import com.afr.fms.Security.UserSecurity.entity.UserSecurity;
import com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper;
import com.afr.fms.Security.UserSecurity.service.UserSecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  @Autowired
  private UserSecurityService userSecurityService;

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private UserTrackerMapper userTrakerMapper;

  @Autowired
  private UserSecurityMapper userSecurityMapper;

  @Autowired
  private SettingMapper settingMapper;

  private Setting setting;

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  boolean flag = false;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    setting = settingMapper.getSetting();
    long lock_time = (long) (((setting.getLock_time() / 1000) / 60) / 60);
    User user = userDetailsService.getUser();
    if (user != null) {
      UserSecurity us = user.getUser_security();
      us.setUser_name(user.getEmail());

      if (user.isStatus() && us.isAccountNonLocked() && !authException.getMessage().startsWith("Full")) {
        if (us.getNumber_of_attempts() < setting.getMaximum_attempt() - 1) {
          userSecurityService.increaseFailedAttempts(us);
        } else {
          userSecurityService.lock(us);
          authException = new LockedException("Your account has been locked due to " + setting.getMaximum_attempt() + " failed attempts." + " It will be unlocked after " + lock_time + " hours. For more info contact with System Administrator");
        }
      } else if (!us.isAccountNonLocked()) {
        if (!userSecurityService.unlockWhenTimeExpired(us)) {
          authException = new LockedException("Your account has been locked. Please try to login again." +  " For more info contact with System Administrator");
        }
      }

      if (!us.isCredentialsNonExpired()) {
        if (!authException.getMessage().startsWith("Bad")) {
          authException = new CredentialsExpiredException("password_expired " + user.getId());
        }

      }
    } else if (!authException.getMessage().startsWith("Full")) {
      String username = userDetailsService.getUsername();
      String ip_address = HttpUtils.clientIp(request);
      UserAgent userAg = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
      String browserType = userAg.getBrowser().getName() + " " + userAg.getBrowserVersion() + " "  + userAg.getOperatingSystem();

      userTrakerMapper.registerUnAutorizedUsers(username, browserType, ip_address);
      userSecurityMapper.addFailedUserName(username);
      flag = true;
    }

    if (!flag && !authException.getMessage().startsWith("Full")) {
      UserAgent userAg = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
      String browserType = userAg.getBrowser().getName() + " " + userAg.getBrowserVersion() + " "
          + userAg.getOperatingSystem();

      userTrakerMapper.registerUnAutorizedUsers(user.getEmail(), browserType, HttpUtils.clientIp(request));
      userSecurityMapper.addFailedUserName(user.getEmail());
    }

    logger.error("Unauthorized error: {}", authException.getMessage());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);

  }

}
