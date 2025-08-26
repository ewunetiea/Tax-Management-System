package com.afr.fms.Admin.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Setting;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.SettingMapper;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@Service
public class SettingService {
    @Autowired
    private SettingMapper settingMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    private RecentActivity recentActivity = new RecentActivity();

    public void manage_account_setting(Setting setting, User user) {

        if (setting.getId() == null) {
            settingMapper.create_account_setting(setting);
        } else {
            settingMapper.updateAccountSetting(setting);

        }
        if (user != null) {
            recentActivity.setMessage("Credential Expiration: " + setting.getCredential_expiration()
                    + ", Maximum Attempt: " + setting.getMaximum_attempt() + ", and Lock Duration: "
                    + setting.getLock_time() + " is configured.");
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);
        }
    }

    public void manage_JWT_setting(Setting setting, User user) {

        if (setting.getId() == null) {
            settingMapper.create_jwt_setting(setting);
        } else {
            settingMapper.updateJWTSetting(setting);
        }

        if (user != null) {
            recentActivity.setMessage("JWT Expiration: " + setting.getJwt_expiration()
                    + ", Refresh Token Expiration: " + setting.getJwt_refresh_token_expiration() + ", and Secret Key: "
                    + setting.getJwt_secret() + " is configured.");
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);
        }
    }

    public Setting getSetting() {
        return settingMapper.getSetting();
    }

}
