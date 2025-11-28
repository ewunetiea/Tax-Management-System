package com.tms.Admin.Controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tms.Admin.Entity.Role;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Service.RoleService;
import com.tms.Common.Permission.Service.FunctionalitiesService;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;

@RestController
@RequestMapping("/api")
public class RoleController {

	@Autowired
	private RoleService roleService;

	RecentActivity recentActivity = new RecentActivity();

	@Autowired
	private RecentActivityMapper recentActivityMapper;

	@Autowired
	private FunctionalitiesService functionalitiesService;

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@PostMapping("/role/admin")
	public ResponseEntity<?> createRole(@RequestBody @Validated Role role, HttpServletRequest request) {
		try {
			roleService.createRole(role);
			User user = functionalitiesService.getUserFromHttpRequest(request);
			if (user != null) {
				// recentActivity.setMessage(" Role: " + role.getName() + role.getId() != null ? " is updated." : " is created.");
				recentActivity.setMessage("Role: " + role.getName() + (role.getId() != null ? " is updated." : " is created."));
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			}
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error creating or updating role: ", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/role")
	public ResponseEntity<List<Role>> getRoles(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(roleService.getRoles(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching roles: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/roles")
	public ResponseEntity<List<Role>> getCommonRoles(HttpServletRequest request) {
		try {

			return new ResponseEntity<>(roleService.getCommonRoles(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while fetching common roles: ", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/roles/{category}")
	public ResponseEntity<List<Role>> getRoles(@PathVariable("category") String category, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(roleService.getRolesByCategory(category), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching roles: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/role/{id}")
	public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(roleService.getRoleById(id), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching role by ID: ", e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("role/delete/admin")
	public ResponseEntity<HttpStatus> deleteRole(@RequestBody List<Role> roles, HttpServletRequest request) {
		try {
			for (Role role : roles) {
				roleService.deactivate_role(role.getId());
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception exception) {
			logger.error("Error deleting roles: ", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("role/activate/admin")
	public ResponseEntity<HttpStatus> activateRole(@RequestBody List<Role> roles, HttpServletRequest request) {
		try {
			for (Role role : roles) {
				roleService.activate_role(role.getId());
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception exception) {
			logger.error("Error activating roles: ", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/role/activate/admin/{id}")
	public ResponseEntity<Boolean> activate_role(@PathVariable Long id, HttpServletRequest request) {
		try {
			roleService.activate_role(id);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception exception) {
			logger.error("Error activating role: ", exception);
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("role/deactivate/admin/{id}")
	public ResponseEntity<Boolean> deactivate_role(@PathVariable Long id, HttpServletRequest request) {

		try {
			roleService.deactivate_role(id);
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} catch (Exception exception) {
			logger.error("Error deactivating role: ", exception);
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
