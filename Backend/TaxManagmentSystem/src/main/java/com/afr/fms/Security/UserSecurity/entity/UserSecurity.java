package com.afr.fms.Security.UserSecurity.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSecurity{
    private Long id;
    private int  number_of_attempts;
    private Long  user_id;
    private Date password_created_date;
    private Date password_modified_date;
    private Date lock_time;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean accountNonExpired;
    private String user_name;
}