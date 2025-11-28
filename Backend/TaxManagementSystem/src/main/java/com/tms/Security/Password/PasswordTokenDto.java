package com.tms.Security.Password;

import lombok.Data;

@Data
public class PasswordTokenDto {
    private String password;
    private String confirmPassword;
    private  String token;
}
