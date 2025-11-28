package com.tms.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.Admin.Entity.User;
import com.tms.Admin.Mapper.UserMapper;
import com.tms.Admin.Mapper.RoleMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserMapper userMapper;

  @Autowired
  RoleMapper userRoleMapper;

  User user;

  String username;

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      this.username = username;
      user = userMapper.findByFusionUsername(username);
      // user.setUsername(user.getEmail());

      return UserDetailsImpl.build(user);
    } catch (Exception e) {
      logger.error("User not found with username: {}", username, e);
      return null;
    }
  }

  public User getUser() {
    return user;
  }

  public String getUsername() {
    return username;
  }
}
