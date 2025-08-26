package com.afr.fms.Admin.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Setting {
   private Long id;
   private int maximum_attempt;
   private Long  lock_time;
   private Long credential_expiration;
   private Long jwt_expiration;
   private Long jwt_refresh_token_expiration;
  private String  jwt_secret;
 }
