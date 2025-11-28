package com.afr.fms.Common.Permission.Service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.RoleMapper;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Common.Entity.Functionalities;
import com.afr.fms.Common.Permission.Mapper.FunctionalitiesMapper;
import com.afr.fms.Security.jwt.JwtUtils;

@Service
public class FunctionalitiesService {

	@Autowired
	private RoleMapper userRoleMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private FunctionalitiesMapper functionalitiesMapper;

	@Autowired
	JwtUtils jwtUtils;

	private static final Logger logger = LoggerFactory.getLogger(FunctionalitiesService.class);

	public boolean verifyPermission(HttpServletRequest request, String functionality_name, String method) {

		// Allowlist for public or whitelisted APIs
		if (functionality_name.contains("/api/auth/signin")
				|| functionality_name.contains("/api/auth/signup")
				|| functionality_name.contains("/api/auth/force-login")
				|| functionality_name.equals("/api/menu/menu")
				|| functionality_name.contains("/api/ws")
				|| functionality_name.contains("upload")
				|| functionality_name.contains("files")
				|| functionality_name.contains("/user/queue")
				|| functionality_name.equals("/api/auth/change-password")
				|| functionality_name.contains("/api/user/role")
				|| functionality_name.contains("/api/signup")
				|| functionality_name.contains("/api/replaceHRData")
				|| functionality_name.contains("/api/user/image")
				|| functionality_name.contains("/api/user/verify")
				|| functionality_name.contains("/api/password/verifyOTP")
				|| functionality_name.contains("/api/branch")
				|| functionality_name.contains("/api/region")
				|| functionality_name.contains("/api/job_position")
				|| functionality_name.contains("/api/selected_job_position")
				|| functionality_name.contains("/api/checkUserEmployeeIdSystem")
				|| functionality_name.contains("/api/checkUserEmployeeId")
				|| functionality_name.contains("/api/checkUserEmail")
				|| functionality_name.contains("/api/checkUsername")
				|| functionality_name.contains("/api/checkUserPhoneNumber")
				|| functionality_name.contains("/api/auth/refreshtoken")
				|| functionality_name.contains("/api/auth/signout")) {
			return true;
		}

		String jwt = jwtUtils.getJwtFromCookies(request);
		logger.info("========== JWT COOKIE CHECK ==========");
		logger.info("URI: " + request.getRequestURI());
		logger.info("JWT from cookie: " + jwt);
		logger.info("======================================");

		// String jwt = jwtUtils.getJwtFromCookies(request);
		if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			logger.info("usernameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee: " + username);
			String normalizedPath = ApiPathNormalizer.normalizeSpringBootPath(request);
			logger.info("Normalized Pathhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh: " + normalizedPath);
			return processVerfyingPermission(username, normalizedPath, method);
		}
		return false;
	}

	public User getUserFromHttpRequest(HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromCookies(request);
		User user = new User();
		if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			user = userMapper.findByEmail(username);
			return user;
		}
		return null;
	}

	public boolean processVerfyingPermission(String username, String functionality_name, String method) {

		List<Role> roles = userRoleMapper.getRolesByUsername(username);
		if (roles == null || roles.isEmpty())
			return false;

		String normalizedName = functionality_name;
		if (functionality_name != null) {
			// Remove anything before /api and ensure it starts with /api
			int apiIndex = functionality_name.indexOf("/api");
			if (apiIndex > 0) {
				normalizedName = functionality_name.substring(apiIndex);
			}

			// Handle param replacement if needed
			if (normalizedName.contains("param")) {
				normalizedName = normalizedName.replace("param", "").trim();
			}
		}
		boolean functionalityExists = false;
		for (Role role : roles) {
			functionalityExists = functionalitiesMapper.checkFunctionalityExists(role.getId(), username, normalizedName,
					method);
			if (functionalityExists) {
				return functionalityExists;
			}
		}
		return false;
	}

	public List<Functionalities> getAllRoleFunctionalities() {
		return functionalitiesMapper.getAllFunctionalities();
	}

	public List<Functionalities> getAllFunctionalitiesByRole(Long id) {
		return functionalitiesMapper.getAllFunctionalitiesByRole(id);
	}

	public void createFunctionality(Functionalities functionalities) {
		try {
			functionalitiesMapper.createFunctionality(functionalities);
		} catch (Exception e) {
			logger.error("Error creating functionality: {}", e.getMessage());
		}
	}

	public boolean existsByNameAndMethod(String name, String method) {
		return functionalitiesMapper.countByNameAndMethod(name, method) > 0;
	}

	public void updateFunctionality(Functionalities functionalities) {
		try {
			functionalitiesMapper.updateFunctionality(functionalities);
		} catch (Exception e) {
			logger.error("Error updating functionality: {}", e.getMessage());
		}
	}

	public void deactivatePermissions(Long id) {
		try {
			functionalitiesMapper.deactivatePermission(id);
		} catch (Exception e) {
			logger.error("Error deactivating permission: {}", e.getMessage());
		}
	}

	public void deletePermission(Long id) {
		try {
			functionalitiesMapper.deletePermission(id);
		} catch (Exception e) {
			logger.error("Error deleting permission: {}", e.getMessage());
		}
	}

	public void activatePermissions(Long id) {
		try {
			functionalitiesMapper.activatePermission(id);
		} catch (Exception e) {
			logger.error("Error activating permission: {}", e.getMessage());
		}
	}

	public void assignPermission(Role role) {
		functionalitiesMapper.deleteRoleFunctionality(role.getId());
		for (Functionalities functionalities : role.getFunctionalities()) {
			functionalitiesMapper.assignPermission(role.getId(), functionalities.getId(), functionalities.isStatus());
		}
	}

	public void changeRolePermisssion(Role role) {
		for (Functionalities functionality : role.getFunctionalities()) {
			functionalitiesMapper.updateRoleFunctionalitiesById(role.getId(), functionality.getId(),
					functionality.isStatus());
		}
	}

	public List<Functionalities> getFunctionalityByCategory(Role role) {
		return functionalitiesMapper.getFunctionalityByCategory(role);
	}

}
