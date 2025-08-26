package com.afr.fms.Common.Validation.Service;

import java.util.List;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Mapper.CopyHRUsersMapper;
import com.afr.fms.Admin.Mapper.JobPositionMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Admin.Entity.JobPosition;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Entity.UserCopyFromHR;

@Service
public class UserValidationService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private JobPositionMapper jobPositionMapper;

	@Autowired
	private CopyHRUsersMapper hrUsersMapper;

	public User checkUserEmail(String email) {
		for (User user : userMapper.getUsers()) {
			try {
				if (user.getEmail().equalsIgnoreCase(email)) {
					return user;
				}
			} catch (Exception e) {

			}
		}
		return null;
	}

	public User checkPhoneNumber(String phone_number) {
		if (phone_number.length() == 10 || phone_number.length() == 13) {
			for (User user : userMapper.getUsers()) {
				try {
					StringJoiner s = new StringJoiner("");
					StringJoiner s2 = new StringJoiner("");
					String zero = "0";
					String prefix = "+251";
					if (phone_number.length() == 13) {
						String phoneNumber = (phone_number.substring(4));
						s.add(zero);
						s.add(phoneNumber);
						if (user.getPhone_number().equalsIgnoreCase(s.toString())) {
							return user;
						}

						else if (user.getPhone_number().equalsIgnoreCase(phone_number)) {
							return user;
						}
					} else if (phone_number.length() == 10) {

						String pn = (phone_number.substring(1));
						s2.add(prefix);
						s2.add(pn);

						if (user.getPhone_number().equalsIgnoreCase(s2.toString())) {
							return user;
						} else if (user.getPhone_number().equalsIgnoreCase(phone_number)) {
							return user;
						}
					}
				} catch (Exception e) {

				}
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
			return null;
		}
	}

	public User fetchUserBranchAndPositionFromHrSystem(User user) {
		try {

		} catch (Exception e) {

		}
		return null;
	}

	public List<JobPosition> getJobPositions() {
		try {

			return hrUsersMapper.get_job_positions();

		} catch (Exception e) {

		}
		return null;
	}
}
