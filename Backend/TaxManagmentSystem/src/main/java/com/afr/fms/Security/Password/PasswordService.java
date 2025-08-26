package com.afr.fms.Security.Password;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
// import java.util.Calendar;
// import java.util.Optional;
// import java.util.UUID;
import java.util.Random;

// import javax.mail.MessagingException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.SMSMapper;
import com.afr.fms.Admin.Service.SMSService;
import com.afr.fms.Admin.Service.UserService;
import com.afr.fms.Security.email.context.ForgotPasswordEmailContext;
import com.afr.fms.Security.email.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PasswordService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    @Value("${jdj.reset.token.validity}")
    private int tokenValidityInSeconds;

    @Autowired
    PasswordMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    PasswordResetTokenMapper passwordResetTokenMapper;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    SMSService smsService;

    @Autowired
    private SMSMapper smsMapper;

    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    public void changePassword(ChangePasswordDto passDto) {
        passDto.setPassword((passwordEncoder.encode(passDto.getPassword())));
        mapper.changePassword(passDto);
    }

    public Boolean passwordMatchesOldPassword(ChangeMyPasswordDto passDto) {

        boolean flag;
        String oldPassword = mapper.getUserPassword(passDto.getId());
        flag = passwordEncoder.matches(passDto.getOldPassword(), oldPassword);
        System.out.println("password " + flag);
        return flag;
    }

    public Boolean passwordDoesnotMatchWithNewPassword(ChangeMyPasswordDto passDto) {
        Boolean flag;
        String oldPassword = mapper.getUserPassword(passDto.getId());
        flag = !passwordEncoder.matches(passDto.getPassword(), oldPassword);
        return flag;
    }

    public void changeMyPassword(ChangeMyPasswordDto passDto) {
        passDto.setPassword(passwordEncoder.encode(passDto.getPassword()));
        mapper.changeMyPassword(passDto.getPassword(), passDto.getId());
        mapper.updateUserSecurity(passDto.getId());
    }

    public Boolean passwordDoesnotMatchWithNewPasswordAD(ChangeMyPasswordDto passDto) {
        Boolean flag;
        flag = !passwordEncoder.matches(passDto.getPassword(), passDto.getOldPassword());
        return flag;
    }

    public User getUserByPasswordResetToken(String token) {
        return passwordResetTokenMapper.getUserByPasswordResetToken(token);
    }

    public void sendPasswordResetTokenEmail(User user, String baseURL) {
        PasswordResetToken myToken = new PasswordResetToken();
        String token = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        myToken.setUser(user);
        myToken.setToken(token);
        myToken.setExpiryDate(LocalDateTime.now().plusSeconds(this.tokenValidityInSeconds));
        passwordResetTokenMapper.saveToken(myToken);

        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.init(user);
        emailContext.setToken(myToken.getToken());
        emailContext.buildVerificationUrl(baseURL, myToken.getToken());

        try {
            emailService.sendMail(emailContext);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void sendPasswordResetviaPhoneNumber(User user) {

        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);

        // Save the new OTP information in the database
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(otp);
        myToken.setExpiryDate(LocalDateTime.now().plusSeconds(this.tokenValidityInSeconds));
        passwordResetTokenMapper.saveToken(myToken);

        // char[] password = userService.generatePassword(8);
        // String password1 = "";
        // for (char p : password) {
        // password1 = password1 + p;
        // }
        // user.setPassword(password1);

        // ChangePasswordDto passDto = new ChangePasswordDto();
        // passDto.setPassword(passwordEncoder.encode(password1));
        // passDto.setId(user.getId());
        // mapper.changePassword(passDto);

        try {
            smsService.sendPasswordResetSMS(user, otp);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    // public void sendPasswordResetviaPhoneNumberAfterAccountCreation(User user) {

    // Random random = new Random();
    // int otpValue = 100_000 + random.nextInt(900_000);
    // String otp = String.valueOf(otpValue);

    // // Save the new OTP information in the database
    // PasswordResetToken myToken = new PasswordResetToken();
    // myToken.setUser(user);
    // myToken.setToken(otp);
    // myToken.setExpiryDate(LocalDateTime.now().plusSeconds(this.tokenValidityInSeconds));
    // passwordResetTokenMapper.saveToken(myToken);

    // try {
    // RestTemplate restTemplate = new RestTemplate();
    // SMS sms = smsMapper.getActiveSMS().get(0);

    // sms.setMessage(
    // "Dear " + user.getFirst_name() + " " + user.getMiddle_name()
    // + ", Your verification code for AFRFMS is "
    // + otp + ". Please do not share this number with anyone. " +
    // "If you have any questions or concerns, please contact our support team. " +
    // "AWASH BANK");
    // String sms_configuration = sms.getApi() + "api?" +
    // "action=sendmessage&username=" + sms.getUser_name()
    // + "&password=" + sms.getPassword().trim() + "&recipient=" +
    // user.getPhone_number()
    // + "&messagetype=SMS:TEXT&messagedata=" + sms.getMessage();

    // String result = restTemplate.getForObject(sms_configuration, String.class);
    // } catch (Exception e) {
    // System.out.println(e);
    // e.printStackTrace();
    // }

    // }

    public void changeUserPassword(User user, String password) {
        mapper.changeMyPassword(passwordEncoder.encode(password), user.getId());
        passwordResetTokenMapper.deleteUserPasswordResetTokens(user.getId());
    }
}
