package com.tms.Common.Validation.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.Admin.Mapper.CopyHRUsersMapper;
import com.tms.Admin.Mapper.JobPositionMapper;
import com.tms.Admin.Mapper.UserMapper;
import com.tms.Admin.Entity.JobPosition;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Entity.UserCopyFromHR;

@Service
public class UserValidationService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private JobPositionMapper jobPositionMapper;

	@Autowired
	private CopyHRUsersMapper hrUsersMapper;

	private static final Logger logger = LoggerFactory.getLogger(UserValidationService.class);

	public User checkUserEmail(String email) {
		try {
			Long id = userMapper.getUserIdByEmail(email);
			User user = new User();
			user.setId(id);
			return user;
		} catch (Exception e) {
			logger.info("Error while checking email:  ", e.getMessage());
		}
		return null;
	}

	public User checkUsername(String username) {
		try {
			User user = userMapper.getUserByUsername(username);
			return user;
		} catch (Exception e) {
			logger.info("Error while checking username:  ", e.getMessage());
		}
		return null;
	}

	public User checkPhoneNumber(String phone_number) {
		if (phone_number.length() == 10 || phone_number.length() == 13) {
			try {
				User user = userMapper.getUserIdByPhoneNumber(phone_number);
				return user;
			} catch (Exception e) {
				logger.info("Error while checking phone:  ", e.getMessage());
			}
		}
		return null;
	}

	public UserCopyFromHR checkUserEmployeeId(String id_no, String year) {
		try {
			String employee_id = "AIB/" + id_no + "/" + year;
			UserCopyFromHR user = hrUsersMapper.checkUserEmployeeId(employee_id);
			if (!user.equals(null)) {
				return user;
			}
		} catch (Exception e) {
			logger.info("Error while checking employee id:  ", e.getMessage());

		}
		return null;
	}

	public UserCopyFromHR checkUserEmployeeId2(String id_no, String year) {

		try {
			String employee_id = "AIB/" + id_no + "/" + year;
			UserCopyFromHR user = hrUsersMapper.checkUserEmployeeId2(employee_id).get(0);
			if (!user.equals(null)) {
				return user;
			}
		} catch (Exception e) {
			logger.info("Error while checking employee id:  ", e.getMessage());

		}
		return null;
	}

	public User checkEmployeeIdSystem(String id_no, String year) {
		try {
			String employee_id = "AIB/" + id_no + "/" + year;
			User user = userMapper.checkEmployeeIdSystem(employee_id);

			if (!user.equals(null)) {
				return user;
			}
		} catch (Exception e) {
			logger.info("Error while checking employee id:  ", e.getMessage());
		}
		return null;
	}

	public String checkJobPositionRole(Long id) {
		try {
			if (jobPositionMapper.checkJobPositionRole(id) != 0) {
				return "Role Exists!";
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.info("Error while checking job position role:  ", e.getMessage());
			return null;
		}
	}

	public List<JobPosition> getJobPositions() {
		try {
			return hrUsersMapper.get_job_positions();
		} catch (Exception e) {
			logger.info("Error while getting job positions:  ", e.getMessage());
		}
		return null;
	}
}
