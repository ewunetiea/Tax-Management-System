package com.afr.fms.Security.UserSecurity.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afr.fms.Admin.Entity.Setting;
import com.afr.fms.Admin.Entity.UserTracker;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Mapper.UserTrackerMapper;
import com.afr.fms.Admin.Service.SettingService;
import com.afr.fms.Security.UserSecurity.entity.RefreshToken;
import com.afr.fms.Security.UserSecurity.exception.TokenRefreshException;
import com.afr.fms.Security.UserSecurity.mapper.RefreshTokenMapper;

@Service
public class RefreshTokenService {
  // @Value("${dreameraba.app.jwtRefreshExpirationMs}")
  // private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenMapper refreshTokenMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserTrackerMapper userTrackerMapper;

  @Autowired
  private SettingService settingService;

  private Setting setting;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenMapper.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId, Long user_tracker_id) {
    setting = settingService.getSetting();
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(userMapper.findById(userId));
    refreshToken.setExpiryDate(Instant.now().plusMillis(setting.getJwt_refresh_token_expiration()));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setUser_tracker_id(user_tracker_id);
    Long id = refreshTokenMapper.save(refreshToken);
    refreshToken = refreshTokenMapper.findById(id);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenMapper.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }
    return token;
  }

  @Transactional
  public void deleteByUserId(Long userId) {
    refreshTokenMapper.deleteByUser(userId);
  }

  @Transactional
  public void deleteByUserTrackerId(Long user_tracker_id) {
    refreshTokenMapper.deleteByUserTrackerID(user_tracker_id);
  }

  public boolean verifyRefreshTokenExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      return true;
    }
    return false;
  }

  public void verifyOnlineUsers() {
    try {
      List<UserTracker> user_status_list = userTrackerMapper.getOnlineUsers();
      for (UserTracker user_status : user_status_list) {
        for (RefreshToken refreshToken : refreshTokenMapper.retrieveRefreshToken(user_status.getUser().getId(),
            user_status.getId())) {
          // if (verifyRefreshTokenExpiration(refreshToken)) {
          userTrackerMapper.registerOfflineUser(user_status.getId());
          // }
        }
      }
    }

    catch (Exception e) {
      
    }
  }
}
