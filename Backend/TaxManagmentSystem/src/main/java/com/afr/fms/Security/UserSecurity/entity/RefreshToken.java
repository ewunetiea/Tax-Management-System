package com.afr.fms.Security.UserSecurity.entity;

import java.time.Instant;
import com.afr.fms.Admin.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshToken {
  private Long id;
  private User user;
  private String token;
  private Instant expiryDate;
  private Long user_tracker_id;
}
