package com.afr.fms.Security.Password;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordTokenSecurityService {

    @Autowired
    private PasswordResetTokenMapper passwordTokenMapper;

    public String validatePasswordResetToken(String token) {
         PasswordResetToken passToken = findByToken(token);  

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken.getExpiryDate()) ? "expired"
                        : null;
    }

    public PasswordResetToken findByToken(String token) {
        PasswordResetToken passToken = passwordTokenMapper.findByToken(token);
        passToken.setExpiryDate(convertToLocalDateTimeViaMilisecond(passToken.getExpire_at()));
        return passToken;
    }

    public LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }
    public boolean isTokenExpired(LocalDateTime expiryDate) {
        return expiryDate.isBefore(LocalDateTime.now()); // this is generic implementation, you can always make it timezone specific
    }

    // private boolean isTokenExpired(PasswordResetToken passToken) {
    //     final Calendar cal = Calendar.getInstance();
    //     return passToken.getExpiryDate().before(cal.getTime());
    // }

}
