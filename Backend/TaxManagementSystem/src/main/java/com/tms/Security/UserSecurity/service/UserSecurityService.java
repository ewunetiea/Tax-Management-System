package com.tms.Security.UserSecurity.service;

import java.util.*;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.Admin.Entity.NotifyAdmin;
import com.tms.Admin.Entity.Setting;
import com.tms.Admin.Service.SettingService;
import com.tms.Payload.realtime.RealTime;
import com.tms.Payload.realtime.RealTimeMapper;
import com.tms.Security.UserSecurity.entity.UserSecurity;
import com.tms.Security.UserSecurity.mapper.UserSecurityMapper;

@Service
@Transactional
public class UserSecurityService {

	@Autowired
	private UserSecurityMapper userSecurityMapper;

	@Autowired
	private RealTimeMapper realTimeMapper;

	@Autowired
	private SettingService settingService;

	private Setting setting;

	private static final Logger logger = LoggerFactory.getLogger(UserSecurityService.class);

	public void increaseFailedAttempts(UserSecurity us) {
		int newFailAttempts = us.getNumber_of_attempts() + 1;
		us.setNumber_of_attempts(newFailAttempts);
		userSecurityMapper.updateAccountLockInfo(us);
		try {
			List<NotifyAdmin> notifyAdmin = userSecurityMapper.isUsernameExist(us.getUser_name());
			if (notifyAdmin != null) {
				userSecurityMapper.updateFailedAttempts(us.getUser_name());
			} else {
				userSecurityMapper.addFailedUserName(us.getUser_name());
			}
		} catch (Exception e) {
			logger.error("increaseFailedAttempts: ", e.getMessage());
		}

	}

	public void resetFailedAttempts(UserSecurity us) {
		us.setNumber_of_attempts(0);
		userSecurityMapper.updateAccountLockInfo(us);
	}

	public void lock(UserSecurity us) {
		us.setAccountNonLocked(false);
		us.setLock_time(new Date());
		userSecurityMapper.updateAccountLockInfo(us);

		try {
			List<NotifyAdmin> notifyAdmin = userSecurityMapper.isUsernameExist(us.getUser_name());
			if (notifyAdmin != null) {
				userSecurityMapper.updateLockedStatus(us.getUser_name());
			} else {
				userSecurityMapper.addLockedUserName(us.getUser_name());
			}
		} catch (Exception e) {
			logger.error("Lock: ", e.getMessage());
		}

	}

	public boolean unlockWhenTimeExpired(UserSecurity us) {
		setting = settingService.getSetting();
		long lockTimeInMillis = us.getLock_time().getTime();
		long currentTimeInMillis = System.currentTimeMillis();
		if ((lockTimeInMillis + setting.getLock_time()) < currentTimeInMillis) {
			us.setAccountNonLocked(true);
			us.setLock_time(null);
			us.setNumber_of_attempts(0);
			userSecurityMapper.updateAccountLockInfo(us);
			return true;
		}
		return false;
	}

	public void checkCredentialTimeExpired(UserSecurity us) {
		setting = settingService.getSetting();
		Date storedDate;
		if (us.getPassword_modified_date() == null) {
			storedDate = us.getPassword_created_date();
		} else {
			storedDate = us.getPassword_modified_date();
		}
		long storedTimeInMillis = storedDate.getTime() / 1000;
		long currentTimeInMillis = System.currentTimeMillis() / 1000;
		if ((currentTimeInMillis - storedTimeInMillis) > setting.getCredential_expiration()) {
			us.setCredentialsNonExpired(false);
			userSecurityMapper.updateCredentialStatus(us);
			try {
				List<NotifyAdmin> notifyAdmin = userSecurityMapper.isUsernameExist(us.getUser_name());
				if (notifyAdmin != null) {
					userSecurityMapper.updateExpiredStatus(us.getUser_name());
				} else {
					userSecurityMapper.addExpiredUserName(us.getUser_name());
				}
			} catch (Exception e) {
				logger.error("checkCredentialTimeExpired: ", e.getMessage());
			}
		}
	}

	public List<NotifyAdmin> notifyAdmin(RealTime realtime) {
		List<NotifyAdmin> notification_list = userSecurityMapper.notifyAdmin();
		if (!notification_list.isEmpty()) {
			Long notification_id = notification_list.get(0).getId();
			if (realtime.getIs_saved()) {
				if (realTimeMapper.retrieveRealTimeInfoNotifyAdmin(realtime) == null) {
					realtime.setLast_notification_admin(notification_id);
					realTimeMapper.insertLastNotificationAdmin(realtime);
				} else {
					Long last_notification_id = realTimeMapper.retrieveRealTimeInfoNotifyAdmin(realtime)
							.getLast_notification_admin();
					if (Long.compare(last_notification_id, notification_id) != 0) {
						realtime.setLast_notification_admin(last_notification_id);
						realTimeMapper.updateRealTimeInfoAdminNotification(realtime);
					}
				}
			} else {
				if (Long.compare(notification_id, realtime.getLast_notification_admin()) == 0) {
					return null;
				} else {
					realtime.setLast_notification_admin(notification_id);
					realTimeMapper.updateRealTimeInfoAdminNotification(realtime);
				}
			}
			return notification_list;
		}
		return new ArrayList<>();
		// return userSecurityMapper.notifyAdmin();
	}

	public void viewedNotificationsByAdmin(List<NotifyAdmin> adminNotification) {
		for (NotifyAdmin notification : adminNotification) {
			if (Integer.compare(notification.getLocked_status(), 1) == 0) {
				userSecurityMapper.viewLockedNotifications(notification.getId());
			}
			if ((Integer.compare(notification.getFailed_status(), 1) == 0)) {
				userSecurityMapper.viewFailedNotifications(notification.getId());
			}
			if ((Integer.compare(notification.getExpired_status(), 1) == 0)) {
				userSecurityMapper.viewExpiredNotifications(notification.getId());
			}
		}

	}

}
