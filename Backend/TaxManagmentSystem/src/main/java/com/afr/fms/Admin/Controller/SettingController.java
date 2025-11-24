package com.afr.fms.Admin.Controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Admin.Entity.Setting;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Service.SettingService;
import com.afr.fms.Security.jwt.JwtUtils;
import com.nimbusds.oauth2.sdk.ParseException;

@RestController
@RequestMapping("/api")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    JwtUtils jwtUtils;

    private User user;

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(SettingController.class);

    @PostMapping("/setting/account/admin")
    public ResponseEntity<?> manage_account_setting(HttpServletRequest request, @RequestBody Setting setting)
            throws ParseException {
        try {

            String jwt = jwtUtils.getJwtFromCookies(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            user = userMapper.findByEmail(username);
            settingService.manage_account_setting(setting, user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            logger.error("Error managing account setting: ", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/setting/jwt/admin")
    public ResponseEntity<?> manage_JWT_setting(HttpServletRequest request, @RequestBody Setting setting)
            throws ParseException {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            user = userMapper.findByEmail(username);
            settingService.manage_JWT_setting(setting, user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            logger.error("Error managing JWT setting: ", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/setting")
    public ResponseEntity<Setting> getSetting(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(settingService.getSetting(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error fetching setting: ", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
