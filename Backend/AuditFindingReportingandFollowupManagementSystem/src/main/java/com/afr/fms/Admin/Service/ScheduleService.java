package com.afr.fms.Admin.Service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.Schedule;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.ScheduleMapper;
import com.afr.fms.Admin.Mapper.RoleMapper;
import com.afr.fms.Common.Entity.Functionalities;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Security.jwt.JwtUtils;

@Service
public class ScheduleService {

	@Autowired
	private RoleMapper userRoleMapper;

	@Autowired
	private ScheduleMapper scheduleMapper;

	@Autowired
	JwtUtils jwtUtils;

	private RecentActivityMapper recentActivityMapper;

	public boolean verifyPermission(HttpServletRequest request, String functionality_name) {
		String jwt = jwtUtils.getJwtFromCookies(request);
		if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			return processVerfyingPermission(username, functionality_name);
		} else {
			return false;
		}
	}

	public boolean processVerfyingPermission(String username, String functionality_name) {
		for (Role role : userRoleMapper.getRolesByUsername(username)) {
			for (Functionalities functionalities : role.getFunctionalities()) {
				if (functionalities.getName().equals(functionality_name)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Schedule> getSchedules() {

		return scheduleMapper.getSchedules();
	}

	public Boolean updateScheduleStatus(String functionality_status_string, User user) {
		try {
			JSONObject functionality_status = new JSONObject(functionality_status_string);
			for (int i = 0; i < functionality_status.names().length(); i++) {

				scheduleMapper.updateScheduleStatusById(
						Long.parseLong(functionality_status.names().getString(i)),
						((Boolean) functionality_status.get(functionality_status.names().getString(i)) == true)
								? 1
								: 0);

			}

			if (user != null) {
				RecentActivity recentActivity = new RecentActivity();
				recentActivity.setMessage("Scheduling setting is modified.");
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			}

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public boolean checkScheduleStatus(String name) {
		if (scheduleMapper.checkScheduleStatus(name) != null) {
			return true;
		}
		return false;
	}

}
