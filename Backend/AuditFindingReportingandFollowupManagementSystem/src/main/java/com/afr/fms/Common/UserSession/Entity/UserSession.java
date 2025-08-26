package com.afr.fms.Common.UserSession.Entity;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    private String username; 
    private Long user_id;
    private Long id;
    private String jwt_token;
    private Timestamp login_time;
    private Timestamp logout_time;
    private String browser_info;
    private String ip_address;
    private boolean active;
    private String session_id;
}
