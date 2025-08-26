package com.afr.fms.Security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;
import com.afr.fms.Security.UserDetailsServiceImpl;
import com.afr.fms.Security.UserSecurity.mapper.RefreshTokenMapper;
import io.jsonwebtoken.UnsupportedJwtException;

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

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    logger.info(" AFRFMS API URI: " + request.getRequestURI() + " HTTP Method: \n " +
        request.getMethod() + " Remote Address: " + request.getRemoteAddr() + " Remote Port: " +
        request.getRemotePort() + " Remote Host: " + request.getRemoteHost() + " Remote User: "
        + request.getRemoteUser());

    // if (functionalitiesService.verifyPermission(request, request.getRequestURI(), request.getMethod())) {
      try {
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);
          User user = userMapper.findByEmail(username);
          if (user == null) {
            throw new UnsupportedJwtException("User is null");
          }
          if (!refreshTokenMapper.isRefreshTokenExist(user.getId())) {
            throw new UnsupportedJwtException("Refresh Token is null");
          }

          logger.info(" AFRFMS JWT is Validated and Username: " + username);
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);

          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
              null,
              userDetails.getAuthorities());

          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
        logger.error("Cannot set user authentication: {}", e.getMessage());
      }
      filterChain.doFilter(request, response);
    // } else {
    //   throw new AccessDeniedException("Permission denied for this resource.");
    // }
  }

  private String parseJwt(HttpServletRequest request) {
    String jwt = jwtUtils.getJwtFromCookies(request);
    return jwt;
  }
}
