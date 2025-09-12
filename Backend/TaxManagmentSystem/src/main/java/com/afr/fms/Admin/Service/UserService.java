package com.afr.fms.Admin.Service;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.RegionMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Mapper.UserRoleMapper;
import com.afr.fms.Admin.Mapper.UserTrackerMapper;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper;
import com.afr.fms.Security.email.context.AccountVerificationEmailContext;
import com.afr.fms.Security.email.service.EmailService;
import com.afr.fms.Security.exception.InvalidTokenException;
import com.afr.fms.Security.token.SecureTokenService;
import com.afr.fms.Security.token.entity.SecureToken;
import com.afr.fms.Security.token.mapper.SecureTokenMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserSecurityMapper userSecurityMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    @Autowired
    private SecureTokenMapper secureTokenMapper;

    @Autowired
    private SecureTokenService secureTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserTrackerMapper userTrackerMapper;;

    @Autowired
    private CopyFromHRSystemService copyFromHRSystemService;

    @Autowired
    private SMSService smsService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RegionMapper regionMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // @Value("${site.base.url.https}")
    private String baseURL = Endpoint.URL;

    public Exception saveUser(User user) {
        if (user.getId() == null) {

            // if (user.isAuthenthication_media()) {

            char[] password = generatePassword(8);

            String password1 = "";

            for (char p : password) {
                password1 = password1 + p;
            }
            user.setPassword(encoder.encode(password1));
            // }
            if (user.getEmployee_id().startsWith("AB")) {
                user.setEmployee_id(user.getEmployee_id().replace("AB", "AIB"));
            }

            Long user_id = userMapper.create_user(user);
            userSecurityMapper.addUserSecurity(user_id);
            user.setId(user_id);

            addAllUserRoles(user);
            user.setPassword(password1);
            try {
                
                if (user.getAdmin_id() != null) {
                    User admin = new User();
                    recentActivity
                            .setMessage("User " + user.getFirst_name() + " " + user.getMiddle_name() + " is added");
                    admin.setId(user.getAdmin_id());
                    recentActivity.setUser(admin);
                    recentActivityMapper.addRecentActivity(recentActivity);
                }
                return null;
            } catch (Exception e) {
                System.out.println(e);
                userRoleMapper.removeAllUserRoles(user.getId());
                secureTokenMapper.deleteByUserId(user_id);
                userSecurityMapper.deleteUserSecurityByUserID(user_id);
                userMapper.deleteUserById(user_id);
                return e;
            }
        } else {
            userMapper.updateUser(user);
            userRoleMapper.removeAllUserRoles(user.getId());
            addAllUserRoles(user);

            User admin = new User();
            recentActivity = new RecentActivity();
            recentActivity.setMessage("User " + user.getFirst_name() + " " +
                    user.getMiddle_name() + " is edited");
            admin.setId(user.getAdmin_id());
            recentActivity.setUser(admin);
            recentActivityMapper.addRecentActivity(recentActivity);
            return null;
        }
    }

    // auto generating password

    public char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }

    public List<User> getUsers() {

        return userMapper.getUsers();
    }

    public List<User> getUserByRoleName(String role_name) {

        return userMapper.getUserByRoleName(role_name);
    }

    public List<User> generatedUsers(User user) {
        return userMapper.generatedUsers(user.getFirst_name(), user.getMiddle_name(), user.getLast_name(),
                user.getEmail(), user.getPhone_number(), user.getCategory(),
                user.getBranch() != null ? user.getBranch().getId() : null,
                user.getRegion() != null ? user.getRegion().getId() : null,
                user.getJobPosition() != null ? user.getJobPosition().getId() : null, user.getGender(),
                user.getEmployee_id());
    }

    public List<User> getUsersStatus() {
        return userMapper.getUsersStatus();
    }

    public List<User> searchUser(String key) {
        return userMapper.searchUser(key);
    }

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    public void addAllUserRoles(User user) {
        // Long id = userMapper.getUserIdByEmail(user.getEmail());
        for (Role role : user.getRoles()) {
            userRoleMapper.addUserRole(user.getId(), role);
        }
    }

    public void manageRoles(User user) {
        userMapper.removeAllUserRoles(user.getId());
        for (Role role : user.getRoles()) {
            userRoleMapper.addUserRole(user.getId(), role);
        }
    }

    public User findByFusionUsername(String email) {
        return userMapper.findByFusionUsername(email);
    }

    public void transferUser(User user) {
        if (user.getBranch() != null && user.getRegion() != null) {
            userMapper.transferUser(user);
        } else if (user.getBranch() != null && user.getRegion() == null) {
            userMapper.transferBranch(user);
        } else if (user.getBranch() == null && user.getRegion() != null) {
            userMapper.transferRegion(user);
        }
    }

    public long getUserIdByEmail(String Email) {
        return userMapper.getUserIdByEmail(Email);
    }

    public String getUserPhotoUrlById(Long id) {
        return userMapper.getPhotoUrlById(id);
    }

    public User findUserByEmail(String email) {
        return userMapper.findByUsername(email);
    }

    public User findUserByVerifiedEmailorPhone(String data, boolean authenthication_media) {
        if (authenthication_media) {
            return userMapper.findByVerifiedEmail(data);

        }
        return userMapper.findByVerifiedPhoneNumber(data);
    }

    public void sendRegistrationConfirmationEmail(User user) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setUser(user);
        secureTokenMapper.save(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendMail(emailContext);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyUser(String token) throws InvalidTokenException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if (Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken())
                || secureToken.isExpired(secureToken.getExpireAt())) {
            throw new InvalidTokenException("Token is not valid");
        }
        User user = secureToken.getUser();
        if (Objects.isNull(user)) {
            return false;
        }
        user.setAccountVerified(true);
        userMapper.accountVerified(user.getId());
        // deleting the invalid token from the database after verfiying the user account
        secureTokenService.removeToken(secureToken);
        return true;
    }

    public void unlockUserAccount(User user, User admin) {
        try {
            if (user.getUser_security().isAccountNonLocked()) {
                userSecurityMapper.updateUnLockAccount(user);
            } else {
                userSecurityMapper.unLockAccount(user);
            }
            userMapper.changeUserStatus(user.getId(), user.isStatus());
            if (admin != null) {
                RecentActivity recentActivity1 = new RecentActivity();
                recentActivity1.setMessage("User : " + user.getEmail() + " status is modified.");
                recentActivity1.setUser(admin);
                recentActivityMapper.addRecentActivity(recentActivity1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void updateUserSecurity(User user, User admin) {
        try {
            if (user.getUser_security().getId() != null) {
                userSecurityMapper.updateUserSecurity(user);
            } else {
                userSecurityMapper.insertUserSecurity(user);
            }

            if (admin != null) {
                RecentActivity recentActivity1 = new RecentActivity();
                recentActivity1.setMessage("User : " + user.getEmail() + " status is modified.");
                recentActivity1.setUser(admin);
                recentActivityMapper.addRecentActivity(recentActivity1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void makeSpecialUser(List<User> users, User admin) {
        try {
            boolean is_special = users.get(0).isSpecial_user();
            for (User user : users) {
                user.setSpecial_user(is_special);
                userMapper.makeSpecialUser(user);
            }

            if (admin != null) {
                RecentActivity recentActivity1 = new RecentActivity();
                recentActivity1.setMessage("Users' category has been updated to 'Special'.");
                recentActivity1.setUser(admin);
                recentActivityMapper.addRecentActivity(recentActivity1);
            }
        } catch (Exception e) {
            logger.error("Error updating special user status for users: {}",
                    admin != null ? admin.getEmail() : null, e);
        }
    }

    public void manageMultipleUsersRole(List<User> users, User admin) {
        try {
            List<Role> roles = (List<Role>) users.get(0).getRoles();
            for (User user : users) {
                user.setRoles(roles);
                manageRoles(user);
            }

            if (roles != null) {
                List<String> rolesName = roles.stream()
                        .map(Role::getName)
                        .collect(Collectors.toList());

                if (rolesName.contains("ROLE_AUDITEE_INS") || rolesName.contains("ROLE_BRANCHM_BFA")) {
                    for (User user : users) {
                        user.setSpecial_user(true);
                        userMapper.makeSpecialUser(user);
                    }
                }
            }

            if (admin != null) {
                RecentActivity recentActivity1 = new RecentActivity();
                recentActivity1.setMessage("Roles for multiple users are adjusted using the Role Modifier.");
                recentActivity1.setUser(admin);
                recentActivityMapper.addRecentActivity(recentActivity1);
            }
        } catch (Exception e) {
            logger.error("Error managing roles for multiple users: {}",
                    admin != null ? admin.getEmail() : null, e);
        }
    }

    public int isSessionExists(String username) {
        return userTrackerMapper.isUserSessionExist(username);
    }

    public void removeSessionFromDB(String username) {
        userTrackerMapper.removeUserSessions(username);
    }

}
