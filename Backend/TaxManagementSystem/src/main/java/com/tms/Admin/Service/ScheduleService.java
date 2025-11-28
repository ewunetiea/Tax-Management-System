package com.tms.Admin.Service;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tms.Admin.Entity.Role;
import com.tms.Admin.Entity.Schedule;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Mapper.ScheduleMapper;
import com.tms.Admin.Mapper.RoleMapper;
import com.tms.Common.Entity.Functionalities;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;
import com.tms.Security.jwt.JwtUtils;

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

	public void updateScheduleStatus(List<Schedule> schedule_status, User user) {
		boolean status = schedule_status.get(0).isStatus();
		for (Schedule schedule : schedule_status) {
			scheduleMapper.updateScheduleStatusById(schedule.getId(), status);
		}

		if (user != null) {
			RecentActivity recentActivity = new RecentActivity();
			recentActivity.setMessage("Schedule setting is modified.");
			recentActivity.setUser(user);
			recentActivityMapper.addRecentActivity(recentActivity);
		}
	}

	public boolean checkScheduleStatus(String name) {
		if (scheduleMapper.checkScheduleStatus(name) != null) {
			return true;
		}
		return false;
	}

}
