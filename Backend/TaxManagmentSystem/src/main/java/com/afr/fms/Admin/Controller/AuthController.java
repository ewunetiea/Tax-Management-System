package com.afr.fms.Admin.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
import com.afr.fms.AD.Service.ADService;
import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Entity.UserSession;
import com.afr.fms.Admin.Mapper.JobPositionMapper;
import com.afr.fms.Admin.Mapper.RoleMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Mapper.UserTrackerMapper;
import com.afr.fms.Admin.Service.RoleService;
import com.afr.fms.Admin.Service.UserService;
import com.afr.fms.Admin.utilis.HttpUtils;
import com.afr.fms.Payload.request.LoginRequest;
import com.afr.fms.Payload.response.MessageResponse;
import com.afr.fms.Payload.response.UserInfoResponse;
import com.afr.fms.Security.UserDetailsImpl;
import com.afr.fms.Security.Password.ChangeMyPasswordDto;
import com.afr.fms.Security.Password.PasswordService;
import com.afr.fms.Security.UserSecurity.entity.RefreshToken;
import com.afr.fms.Security.UserSecurity.entity.UserSecurity;
import com.afr.fms.Security.UserSecurity.exception.TokenRefreshException;
import com.afr.fms.Security.UserSecurity.service.RefreshTokenService;
import com.afr.fms.Security.UserSecurity.service.UserSecurityService;
import com.afr.fms.Security.WebSocket.SessionManager;
import com.afr.fms.Security.WebSocket.UserSessionRepository;
import com.afr.fms.Security.exception.UserNotFoundException;
import com.afr.fms.Security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        @PostMapping("/signin")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                        HttpServletRequest request) throws Exception {

                String username = loginRequest.getUsername();

                // Step 1: AD authentication
                // try {
                // boolean userDetail = adService.authenticateUser(loginRequest.getUsername(),
                // loginRequest.getPassword());

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

                // Step 2: Check if user already has an active session
                // List<UserSession> sessions = userSessionRepository.findByUserName(username);
                // if (sessions != null && sessions.size() > 0) {
                // throw MultipleSessionsException.forUser(username, sessions.size());
                // }

                // Step 3: Proceed with normal login
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

                                UserSecurity us = user.getUser_security();
                                userSecurityService.checkCredentialTimeExpired(us);
                        }
                } catch (Exception e) {
                        logger.error("Error checking user credential expiration for username: {}",
                                        loginRequest.getUsername(), e);
                }

                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(
                                                loginRequest.getUsername(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                // Generate tokens
                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails, request);

                String ipAddress = HttpUtils.clientIp(request);

                // Register login tracker
                Long id_login_tracker = userTrakerMapper.registerOnlineUser(
                                loginRequest.getUsername(),
                                loginRequest.getUserAgent(),
                                ipAddress);
                // Register session in DB

                // sessionManager.registerSession(loginRequest.getUsername(), id_login_tracker);

                // Prepare response
                List<String> roles = userDetails.getAuthorities().stream()
                                .map(item -> item.getAuthority())
                                .collect(Collectors.toList());

                String title = userMapper.findByFusionUsername(loginRequest.getUsername())
                                .getJobPosition().getTitle();

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(
                                userDetails.getId(),
                                id_login_tracker);

                ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken(), request);

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
                                                userDetails.getCategory(),
                                                userDetails.getBranch(),
                                                userDetails.getRegion(),
                                                userDetails.getBanking(),
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

                        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie(request);
                        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie(request);

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
                                        roles = jobPositionMapper.getRoleByJobPositionId(user.getJobPosition().getId(), user.getCategory());
                                        user.setSpecial_user(false);
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
