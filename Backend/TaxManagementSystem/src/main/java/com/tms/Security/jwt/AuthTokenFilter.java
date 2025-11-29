package com.tms.Security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Mapper.UserMapper;
import com.tms.Admin.Mapper.UserTrackerMapper;
import com.tms.Common.Permission.Service.FunctionalitiesService;
import com.tms.Security.UserDetailsServiceImpl;
import com.tms.Security.UserSecurity.mapper.RefreshTokenMapper;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  UserMapper userMapper;

  @Autowired
  FunctionalitiesService functionalitiesService;

  @Autowired
  RefreshTokenMapper refreshTokenMapper;

  @Autowired
  UserTrackerMapper userTrackerMapper;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Autowired
  private AllowListProperties allowListProperties;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String hostHeader = request.getHeader("Host");
    String originHeader = request.getHeader("Origin");
    String remoteAddr = request.getRemoteAddr();

    logger.info("tms API URI: {} Method: {} Remote: {} Host: {} Origin: {}",
        request.getRequestURI(), request.getMethod(), remoteAddr, hostHeader, originHeader);

    // 🔒 Block if not from allowed hosts/origins/IPs
    // if (!isAllowed(hostHeader, originHeader, remoteAddr)) {
    //   logger.warn("Blocked request from disallowed source. Host={} Origin={} IP={}", hostHeader, originHeader, remoteAddr);
    //   response.sendError(HttpServletResponse.SC_FORBIDDEN, "Blocked by strict origin/IP policy");
    //   return;
    // }

    // ✅ Permission + JWT check
    // if (functionalitiesService.verifyPermission(request, request.getRequestURI(), request.getMethod())) {
      try {
        String jwt = parseJwt(request);

        System.out.println("jwt is ______________________________" + jwt);

        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
          User user = userMapper.findByEmail(username);
          
          if (user == null) {
            throw new UnsupportedJwtException("User not found");
          }

          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
        logger.error("Cannot set user authentication: {}", e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
      filterChain.doFilter(request, response);
    // } else {
    //   throw new AccchaessDeniedException("Permission denied for this resource.");
    // }
  }

  private String parseJwt(HttpServletRequest request) {
    return jwtUtils.getJwtFromCookies(request);
  }

  private boolean isAllowed(String hostHeader, String originHeader, String remoteAddr) {
    // Host header check
    if (hostHeader != null && allowListProperties.getHosts().contains(hostHeader)) {
      return true;
    }

    // Origin header check
    if (originHeader != null) {
      for (String allowed : allowListProperties.getHosts()) {
        if (originHeader.contains(allowed)) {
          return true;
        }
      }
    }

    // Remote IP check
    return allowListProperties.getIps().contains(remoteAddr);
  }

}
