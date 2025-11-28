package com.tms.Security.Password;

import java.net.URI;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tms.Admin.Entity.User;
import com.tms.Admin.Service.UserService;

@RestController
@RequestMapping("api/password")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordTokenSecurityService passwordTokenSecurityService;

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> changePassword(@RequestBody @Validated ChangePasswordDto passDto) {
        try {
            passwordService.changePassword(passDto);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error occurred during changing password ", e);
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/changeMyPassword", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> changeMyPassword(@RequestBody @Validated ChangeMyPasswordDto passDto) {
        try {
            if (!(passwordService.passwordMatchesOldPassword(passDto)
                    && passwordService.passwordDoesnotMatchWithNewPassword(passDto))) {

                return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
            }

            passwordService.changeMyPassword(passDto);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred during changing password ", e);
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping(value = "/forgotPassword")
    public ResponseEntity<HttpStatus> resetPassword(HttpServletRequest request,
            @RequestParam("email") String userEmail, @RequestParam("phone_number") String phone_number,
            @RequestParam("authenthication_media") boolean authenthication_media) {

        User user = new User();
        if (authenthication_media) {
            user = userService.findUserByVerifiedEmailorPhone(userEmail, authenthication_media);
        } else {
            user = userService.findUserByVerifiedEmailorPhone(phone_number, authenthication_media);
        }
        if (user == null && authenthication_media) {
            throw new UsernameNotFoundException("Email not found");
        } else if (user == null && !authenthication_media) {
            throw new UsernameNotFoundException("Phone Number not found");
        }

        try {
            if (authenthication_media) {
                // final String appUrl = "https://10.10.101.76:8000/afrfmsbackend/api/password";
                final String appUrl = "https://afrfmsbackendlb.awashbank.com/afrfmsbackend/api/password";
                passwordService.sendPasswordResetTokenEmail(user, appUrl);
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            } else {
                passwordService.sendPasswordResetviaPhoneNumber(user);
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            }

        } catch (MailException ex) {
            logger.error("Error occurred during validating authentication media : {}",
                    userEmail != null ? userEmail : phone_number, ex);
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/changePassword")
    public ResponseEntity<Void> showChangePasswordPage(@RequestParam("token") String token) {
        try {
            String result = passwordTokenSecurityService.validatePasswordResetToken(token);
            if (result != null) {

                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("https://audit.awashbank.com/afrfms/invalid-token")).build();
                // .location(URI.create("https://10.10.101.76:8000/afrfms/invalid-token")).build();

            } else {

                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("https://audit.awashbank.com/afrfms/update-password?token=" + token))
                        // .location(URI.create("https://10.10.101.76:8000/afrfms/update-password?token="
                        // + token))
                        .build();

            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("https://audit.awashbank.com/afrfms/invalid-token")).build();
            // .location(URI.create("https://10.10.101.76:8000/afrfms/invalid-token")).build();

        }

    }

    @GetMapping("/verifyOTP")
    public ResponseEntity<String> showChangePasswordPageOTP(@RequestParam("token") String token) {
        try {
            String result = passwordTokenSecurityService.validatePasswordResetToken(token);
            if (result != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(token, HttpStatus.OK);
            }

        } catch (Exception e) {
            logger.error("Error occurred during validating OTP token : {}", token, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<HttpStatus> savePassword(@RequestBody @Validated PasswordTokenDto passwordTokenDto) {
        String result = passwordTokenSecurityService.validatePasswordResetToken(passwordTokenDto.getToken());

        if (!passwordTokenDto.getPassword().equals(passwordTokenDto.getConfirmPassword())) {
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            if (result != null) {
                return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            User user = passwordService.getUserByPasswordResetToken(passwordTokenDto.getToken());
            if (user != null) {
                passwordService.changeUserPassword(user, passwordTokenDto.getPassword());
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            } else {
                return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/getUserByResetToken")
    public ResponseEntity getUserByResetToken(@RequestParam("token") String token) {
        String result = passwordTokenSecurityService.validatePasswordResetToken(token);
        if (result != null) {
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        } else {
            User user = passwordService.getUserByPasswordResetToken(token);
            if (user != null) {
                // passwordService.changeUserPassword(user.get(),
                // passwordTokenDto.getNewPassword());
                return new ResponseEntity(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
            }
        }
    }

}