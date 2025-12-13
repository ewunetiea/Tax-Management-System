package com.tms.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.tms.Security.jwt.AuthEntryPointJwt;
import com.tms.Security.jwt.AuthTokenFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        .requestMatchers(
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**")
        .permitAll()
        .requestMatchers("/api/**").permitAll()
        .anyRequest().authenticated();
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  // @Bean
  //   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  //       http
  //           .cors().and() .csrf().disable() // If you use JWT, CSRF can remain disabled
  //           .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
  //           .and()
  //           .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
  //           .and()
  //           .headers(headers -> headers
  //               .xssProtection(xss -> xss.block(true)) // Enable browser XSS filter
  //               .contentSecurityPolicy(csp -> csp.policyDirectives(
  //                   "default-src 'self'; " +
  //                   "script-src 'self'; " +
  //                   "style-src 'self' 'unsafe-inline'; " +
  //                   "img-src 'self' data:;" +
  //                   "font-src 'self';"
  //               ))
  //               .frameOptions(frame -> frame.sameOrigin())
  //               .contentTypeOptions() // Prevent MIME sniffing
  //           )
  //           .authorizeHttpRequests(auth -> auth
  //               .requestMatchers(
  //                   "/v3/api-docs/**",
  //                   "/swagger-ui.html",
  //                   "/swagger-ui/**",
  //                   "/swagger-resources/**",
  //                   "/webjars/**"
  //               ).permitAll()
  //               .requestMatchers("/api/auth/**").permitAll() // Login/Register
  //               .requestMatchers("/api/**").authenticated()
  //               .anyRequest().authenticated()
  //           );

  //       http.authenticationProvider(authenticationProvider());
  //       http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

  //       return http.build();
  //   }
}
