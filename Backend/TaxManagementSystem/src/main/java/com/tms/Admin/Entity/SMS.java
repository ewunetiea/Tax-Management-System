package com.tms.Admin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMS {
    private Long id;
    private String user_name;
    private String password;
    private String message;
    private String api;
    private Boolean status;
    private Long user_id;
    private String system_type; // ad, sms, email, etc.
}
