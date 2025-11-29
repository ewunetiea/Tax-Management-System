package com.tms.Admin.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.tms.AD.Service.ADService;
import com.tms.AD.Service.LDAPProductionService;
import com.tms.Admin.Entity.Role;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Mapper.JobPositionMapper;
import com.tms.Admin.Mapper.RoleMapper;
import com.tms.Admin.Mapper.UserMapper;
import com.tms.Admin.Mapper.UserTrackerMapper;
import com.tms.Admin.Service.RoleService;
import com.tms.Admin.Service.UserService;
import com.tms.Admin.utilis.HttpUtils;
import com.tms.Payload.request.LoginRequest;
import com.tms.Payload.response.MessageResponse;
import com.tms.Payload.response.UserInfoResponse;
import com.tms.Security.UserDetailsImpl;
import com.tms.Security.Password.ChangeMyPasswordDto;
import com.tms.Security.Password.PasswordService;
import com.tms.Security.UserSecurity.entity.RefreshToken;
import com.tms.Security.UserSecurity.exception.TokenRefreshException;
import com.tms.Security.UserSecurity.service.RefreshTokenService;
import com.tms.Security.UserSecurity.service.UserSecurityService;
import com.tms.Security.WebSocket.SessionManager;
import com.tms.Security.WebSocket.UserSessionRepository;
import com.tms.Security.exception.UserNotFoundException;
import com.tms.Security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        PasswordEncoder encoder;

        @Autowired
        UserService userService;

        @Autowired
        RoleService roleService;

        @Autowired
        UserSecurityService userSecurityService;

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        RefreshTokenService refreshTokenService;

        @Autowired
        private JobPositionMapper jobPositionMapper;

        @Autowired
        private UserMapper userMapper;

        @Autowired
        private UserTrackerMapper userTrakerMapper;

        @Autowired
        private RoleMapper roleMapper;

        @Autowired
        private UserSessionRepository userSessionRepository;

        @Autowired
        private ADService adService;

        @Autowired
        private PasswordService passwordService;

        @Autowired
        private SessionManager sessionManager;

        @Autowired
        private LDAPProductionService lDAPProductionService;

        @PostMapping("/signin")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                        HttpServletRequest request) throws Exception {


                // Step 1: AD authentication
               
                // try {


                // // boolean userDetail = adService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

                // boolean userDetail =   lDAPProductionService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

                

                // if (!userDetail) {
                // throw new UserNotFoundException(
                // "Invalid credentials. Please provide the correct username and password.");
                // }
                // } catch (UserNotFoundException e) {
                // logger.error("User not found for username: {}", loginRequest.getUsername(),
                // e.getMessage());
                // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new
                // MessageResponse(e.getMessage()));
                // } catch (Exception e) {
                // logger.error("Error while validating ad for username: {}",
                // loginRequest.getUsername(), e.getMessage());
                // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                // .body(new MessageResponse(
                // "An error occurred during authentication. Please try again later."));
                // }



                return doLogin(loginRequest, request);
        }

        private ResponseEntity<?> doLogin(LoginRequest loginRequest, HttpServletRequest request) {
                try {
                        User user = userService.findByFusionUsername(loginRequest.getUsername());
                        if (user != null) {
                                ChangeMyPasswordDto passDto = new ChangeMyPasswordDto();
                                passDto.setId(user.getId());
                                passDto.setPassword(loginRequest.getPassword());
                                passDto.setOldPassword(user.getPassword());

                                if (passwordService.passwordDoesnotMatchWithNewPasswordAD(passDto)) {
                                        passwordService.changeMyPassword(passDto);
                                }

                                // the follwing code commented due to LDAP
                                // UserSecurity us = user.getUser_security();
                                // userSecurityService.checkCredentialTimeExpired(us);
                        }
                } catch (Exception e) {
                        logger.error("Error checking user credential expiration for username: {}", loginRequest.getUsername(), e);
                }

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                // Generate tokens
                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails, request);
                String ipAddress = HttpUtils.clientIp(request);

                // Register login tracker
                Long id_login_tracker = userTrakerMapper.registerOnlineUser( loginRequest.getUsername(), loginRequest.getUserAgent(), ipAddress);
               

                List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

                String title = userMapper.findByFusionUsername(loginRequest.getUsername()).getJobPosition().getTitle();
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), id_login_tracker);
                ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken(), request);

                System.out.println("__________Refresh token_________________");

                System.out.println(jwtRefreshCookie);

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                                .body(new UserInfoResponse(
                                                userDetails.getId(),
                                                id_login_tracker,
                                                userDetails.getUsername(),
                                                userDetails.getEmail(),
                                                userDetails.getPhotoUrl(),
                                                title,
                                                userDetails.getBranch(),
                                                userDetails.getRegion(),
                                                roles));
        }

        @PostMapping("/force-login")
        public ResponseEntity<?> forceLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
                try {
                        // Invalidate all existing sessions for this user
                        // sessionManager.invalidateAllSessions(loginRequest.getUsername());
                        return doLogin(loginRequest, request);
                } catch (Exception e) {
                        logger.error("Force login failed for username: {}", loginRequest.getUsername(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new MessageResponse("Force login failed. Please try again."));
                }
        }

        @GetMapping("/signout/{id_login_tracker}")
        public ResponseEntity<?> logoutUser(@PathVariable("id_login_tracker") Long id_login_tracker,
                        HttpServletRequest request) {
                try {
                        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        if (!(principle.toString().equals("anonymousUser"))) {
                                // Delete refresh token
                                refreshTokenService.deleteByUserTrackerId(id_login_tracker);

                                if (principle instanceof UserDetailsImpl) {
                                        String username = ((UserDetailsImpl) principle).getUsername();
                                        // sessionManager.invalidateSession(username, id_login_tracker);
                                }
                        }

                        
                        ResponseCookie jwtCookie = jwtUtils.clearJwtCookie(request);
                        ResponseCookie jwtRefreshCookie = jwtUtils.clearJwtRefreshCookie(request);

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                        .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                                        .body(new MessageResponse("You've been signed out!"));
                } catch (Exception e) {
                        logger.error("Logout failed for tracker: {}", id_login_tracker, e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new MessageResponse("Logout failed. Please try again."));
                }
        }

        @PostMapping("/refreshtoken")
        public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
                try {
                        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
                        if ((refreshToken != null) && (refreshToken.length() > 0)) {
                                return refreshTokenService.findByToken(refreshToken)
                                                .map(refreshTokenService::verifyExpiration)
                                                .map(RefreshToken::getUser)
                                                .map(user -> {
                                                        user.setUsername(user.getEmail());
                                                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user,
                                                                        request);
                                                        return ResponseEntity.ok()
                                                                        .header(HttpHeaders.SET_COOKIE,
                                                                                        jwtCookie.toString())
                                                                        .body(new MessageResponse(
                                                                                        "JWT is refreshed successfully!"));
                                                })
                                                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                                                                "Refresh token is not in database!"));
                        }
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                        .body(new MessageResponse("Refresh Token is empty!"));
                } catch (Exception e) {
                        logger.error("Error occurred during token refresh", e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(
                                        "An error occurred during token refresh. Please try again later."));
                }
        }

        @Transactional
        @PostMapping("/signup")
        public ResponseEntity<?> signup(@RequestBody User user) {
                try {
                        List<Role> roles = new ArrayList<>();
                        if (user != null) {
                                if (user.getJobPosition() != null && user.getJobPosition().getId() != null) {
                                        roles = jobPositionMapper.getRoleByJobPositionId(user.getJobPosition().getId());

                                        user.setRoles(roles);
                                }
                        }

                        userService.saveUser(user);

                        return new ResponseEntity<>(HttpStatus.ACCEPTED);

                } catch (Exception e) {
                        logger.error("Error occurred during signup for employee ID: {}", user.getEmployee_id(), e);
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
        }
}
