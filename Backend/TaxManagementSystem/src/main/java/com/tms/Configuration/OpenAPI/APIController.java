package com.tms.Configuration.OpenAPI;

import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/api-manager")
public class APIController {

    @Autowired
    private SwaggerApiRegistrar swaggerApiRegistrar;

    @PostMapping("/register-apis")
    public ResponseEntity<String> registerSwaggerApis() {


        
        swaggerApiRegistrar.registerSwaggerApis();
        return ResponseEntity.ok("Swagger APIs registered successfully.");
    }
}
