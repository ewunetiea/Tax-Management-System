package com.afr.fms.Admin.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.JobPositionMapper;
import com.afr.fms.Admin.Mapper.RoleMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Service.CopyFromHRSystemService;
import com.afr.fms.Admin.Service.UserService;
import com.afr.fms.Admin.utilis.ImageHandlerUtil;
import com.afr.fms.Admin.utilis.ImageService;
import com.afr.fms.Payload.response.AGPResponse;
import com.afr.fms.Security.exception.NoUsersAvailableException;
import com.afr.fms.Security.exception.UserNotFoundException;
import com.afr.fms.Security.jwt.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CopyFromHRSystemService copyFromHRSystemService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user")
    public ResponseEntity<List<User>> get_users(HttpServletRequest request) throws NoUsersAvailableException {
        try {
            return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during fetching users", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/user/replaceHRData")
    public ResponseEntity<HttpStatus> replaceHRData(HttpServletRequest request) throws NoUsersAvailableException {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            copyFromHRSystemService.replacingHRData();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during replacing HR Data:", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/generate")
    public ResponseEntity<List<User>> generatedUsers(HttpServletRequest request, @RequestBody User user)
            throws NoUsersAvailableException {
        try {
            return new ResponseEntity<>(userService.generatedUsers(user), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during generating users", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/user_status")
    public ResponseEntity<List<User>> getUsersStatus(HttpServletRequest request) throws NoUsersAvailableException {
        try {
            return new ResponseEntity<>(userService.getUsersStatus(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during fetching users' status", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(path = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AGPResponse> create_user(HttpServletRequest request, @RequestBody User user) {

        try {
            List<Role> roles = new ArrayList<>();
            if (user != null) {
                roles = jobPositionMapper.getRoleByJobPositionId(user.getJobPosition().getId(), user.getCategory());
                if (roles != null) {
                    try {
                        List<String> rolesName = roles.stream()
                                .map(Role::getName)
                                .collect(Collectors.toList());

                        if (rolesName.contains("ROLE_AUDITEE_INS")) {
                            roles.add(roleMapper.getRoleByCode("BRANCHM_BFA"));
                            user.setSpecial_user(true);
                        } else if (rolesName.contains("ROLE_BRANCHM_BFA")) {
                            roles.add(roleMapper.getRoleByCode("AUDITEE_INS"));
                            user.setSpecial_user(true);
                        } else {
                            user.setSpecial_user(false);
                        }

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }

                user.setRoles(roles);
            }

            Exception e = userService.saveUser(user);

            if (e == null) {
                return AGPResponse.success("SUCCESS");
            } else {
                return AGPResponse.error("Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            logger.error("Error occurred during creating user account for : {}",
                    user != null ? user.getEmployee_id() : null,
                    ex);
            return AGPResponse.error("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(path = "/user", consumes = { "application/json", "multipart/form-data" })
    public ResponseEntity<AGPResponse> updateUser(HttpServletRequest request,
            @RequestPart(name = "user_data") String user_data,
            @RequestParam("image") MultipartFile multipartFile)
            throws ParseException, JsonProcessingException, IOException {
        try {
            User user = new ObjectMapper().readValue(user_data, User.class);
            if (!multipartFile.isEmpty()) {
                ImageHandlerUtil imageHandlerUtil = this.imageService.uploadImage(multipartFile, "user");
                user.setPhotoUrl(imageHandlerUtil.getName());
            } else {

                if (user.getPhotoUrl() == null) {
                    user.setPhotoUrl(null);
                }
            }
            userService.saveUser(user);
            return AGPResponse.success("SUCCESS");
        } catch (DuplicateKeyException ex) {
            User user = new ObjectMapper().readValue(user_data, User.class);
            logger.error("Error occurred during updating user account for : {}",
                    user != null ? user.getEmployee_id() : null, ex);
            return AGPResponse.error("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/searchUser")
    public ResponseEntity<List<User>> searchUser(HttpServletRequest request, @RequestParam String key) {
        try {
            return new ResponseEntity<>(userService.searchUser(key), HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/roles")
    public ResponseEntity<HttpStatus> manageRoles(HttpServletRequest request, @RequestBody User user) {
        try {
            userService.manageRoles(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during updating role for : {}", user != null ? user.getEmployee_id() : null,
                    ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/multiple_user_role")
    public ResponseEntity<HttpStatus> manageMultipleUsersRole(HttpServletRequest request,
            @RequestBody List<User> users) {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            User user1 = userMapper.findByEmail(username);
            userService.manageMultipleUsersRole(users, user1);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during updating roles for users : {}", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/branch")
    public ResponseEntity<HttpStatus> transferBranch(HttpServletRequest request, @RequestBody User user) {
        try {
            userService.transferUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during transferring branch for : {}",
                    user != null ? user.getEmployee_id() : null,
                    ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> get_user_by_Id(HttpServletRequest request, @PathVariable("id") long id)
            throws UserNotFoundException {
        User userData = userService.getUserById(id);
        if (userData != null) {
            return new ResponseEntity<>(userData, HttpStatus.OK);
        } else {
            throw UserNotFoundException.forUser(Long.toString(id));
        }

    }

    @GetMapping("/users/{role_name}")
    public ResponseEntity<List<User>> getUserByRoleName(HttpServletRequest request,
            @PathVariable("role_name") String role_name)
            throws UserNotFoundException {
        List<User> userData = userService.getUserByRoleName(role_name);
        if (userData != null) {
            return new ResponseEntity<>(userData, HttpStatus.OK);
        } else {
            throw UserNotFoundException.forUser(role_name);
        }

    }

    @GetMapping(value = "user/image/{photoUrl}", produces = MediaType.IMAGE_PNG_VALUE)
    private ResponseEntity<Object> getImage(HttpServletRequest request, @PathVariable("photoUrl") String photoUrl)
            throws IOException {
        ImageHandlerUtil imageHandlerUtil = this.imageService.getImage(photoUrl);
        imageHandlerUtil.setUserType("user");
        File file = new File(imageHandlerUtil.getFilePath());
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(
                            MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("Couldn't find " + file.getName() +
                            " => " + e.getMessage());
        }

    }

    @GetMapping("/user/verify")
    public ResponseEntity<String> verifyUserAccount(HttpServletRequest request,
            @RequestParam(required = false) String token) {
        if (StringUtils.isEmpty(token)) {

            // return ResponseEntity.status(HttpStatus.FOUND)
            // .location(URI.create("https://10.10.101.76:8000/afrfms/invalid-token")).build();

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("https://audit.awashbank.com/afrfms/invalid-token")).build();

        }
        try {
            userService.verifyUser(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("https://audit.awashbank.com/afrfms/invalid-token")).build();
            // .location(URI.create("https://10.10.101.76:8000/afrfms/invalid-token")).build();

        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("https://audit.awashbank.com/afrfms")).build();
        // .location(URI.create("https://10.10.101.76:8000/afrfms")).build();
    }

    @PostMapping("/user/account")
    public ResponseEntity<HttpStatus> unlockUser(HttpServletRequest request, @RequestBody User user) {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            User user1 = userMapper.findByEmail(username);
            // userService.unlockUserAccount(user, user1);
            userService.updateUserSecurity(user, user1);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during managing user security for : {}",
                    user != null ? user.getEmployee_id() : null, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/user/special")
    public ResponseEntity<HttpStatus> makeSpecialUser(HttpServletRequest request, @RequestBody List<User> users) {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            User admin = userMapper.findByEmail(username);
            userService.makeSpecialUser(users, admin);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error occurred during making users special ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
