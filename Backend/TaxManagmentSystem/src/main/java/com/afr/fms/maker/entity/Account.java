package com.afr.fms.Maker.entity;

import com.afr.fms.Admin.Entity.User;
import lombok.Data;

@Data
public class Account {
  private Long id;
  private String accountName;
  private String accountType;
  private String email;
  private String phone;
  private String status;
  private Transaction transaction;
  private User user;
  private Long user_id;
}