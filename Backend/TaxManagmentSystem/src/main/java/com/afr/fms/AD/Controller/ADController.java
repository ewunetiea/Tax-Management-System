package com.afr.fms.AD.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Payload.request.LoginRequest;
import com.afr.fms.AD.Service.ADService;

@RestController
@RequestMapping("/api/ad")
public class ADController {
    @Autowired
    private ADService adService;

    @GetMapping("/validate_user")
    public ResponseEntity<Object> validate_user( @Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Object user = adService.getADUserDetails(loginRequest.getUsername(), loginRequest.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println(ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
