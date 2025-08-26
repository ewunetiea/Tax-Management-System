package com.afr.fms.Security.Password;

import java.time.LocalDateTime;
import java.util.Date;

import com.afr.fms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    public static final int EXPIRATION_TIME = 10; //in minutes

    private Long id;

    private String token;

    private User user;

    private Date expire_at;

    // @Column(updatable = false)
    // @Basic(optional = false)
    private LocalDateTime expiryDate;

   
    public boolean isExpired(LocalDateTime expiryDate) {
        return getExpiryDate().isBefore(LocalDateTime.now()); // this is generic implementation, you can always make it timezone specific
    }
    
}
