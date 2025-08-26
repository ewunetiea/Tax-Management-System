package com.afr.fms.Security.token.entity;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.*;

// import org.hibernate.annotations.CreationTimestamp;

import com.afr.fms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecureToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(unique = true)
    private String token;

    // @CreationTimestamp
    // @Column(updatable = false)
    private Timestamp timeStamp;

    private Date expire_at;

    // @Column(updatable = false)
    // @Basic(optional = false)
    private LocalDateTime expireAt;

    // @ManyToOne
    // @JoinColumn(name = "user_id", referencedColumnName ="id")
    private User user;

   
    public boolean isExpired(LocalDateTime expireAt) {
        return getExpireAt().isBefore(LocalDateTime.now()); // this is generic implementation, you can always make it timezone specific
    }
}
