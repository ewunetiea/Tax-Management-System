package com.afr.fms.Common.Permission.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Common.Entity.Functionalities;
import com.afr.fms.Common.Permission.Service.UserFunctionalityService;
import com.nimbusds.oauth2.sdk.ParseException;

@RestController
@RequestMapping("/api/user-permission")
public class UserFunctionalityController {

    @Autowired
    private UserFunctionalityService userFunctionalityService;

    private static final Logger logger = LoggerFactory.getLogger(UserFunctionalityController.class);

    @PostMapping("/activate")
    public ResponseEntity<?> activateUserFunctionality(HttpServletRequest request,
            @RequestBody List<Functionalities> functionalities) throws ParseException {
        try {
            userFunctionalityService.deleteUserFunctionality(functionalities);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while activating user functionality", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deactivate")
    public ResponseEntity<HttpStatus> deactivateUserFunctionality(HttpServletRequest request, @RequestBody User user)
            throws ParseException {
        try {
            userFunctionalityService.createUserFunctionality(user);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception ex) {
            logger.error("Error while deactivating user functionality", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestBody User user, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(userFunctionalityService.generatedUsers(user), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching all user functionalities", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
