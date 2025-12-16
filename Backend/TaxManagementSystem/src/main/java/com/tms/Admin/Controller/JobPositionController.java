package com.tms.Admin.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tms.Admin.Entity.JobPosition;
import com.tms.Admin.Entity.JobPositionRole;
import com.tms.Admin.Entity.Role;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Service.JobPositionService;
import com.tms.Common.Permission.Service.FunctionalitiesService;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;

@RestController
@RequestMapping("/api")
public class JobPositionController {

	@Autowired
	private JobPositionService jobPositionService;

	RecentActivity recentActivity = new RecentActivity();

	@Autowired
	private RecentActivityMapper recentActivityMapper;

	@Autowired
	private FunctionalitiesService functionalitiesService;

	private static final Logger logger = LoggerFactory.getLogger(JobPositionController.class);

	@GetMapping("/job_position/byRole")
	public ResponseEntity<JobPositionRole> getJobPositionsByRole(HttpServletRequest request, @RequestBody Role role) {
		try {
			JobPositionRole jobPositionsByRole = jobPositionService.getJobPositionsByRole(role);
			return new ResponseEntity<>(jobPositionsByRole, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/jobPosition/manageJobPositions")
	public ResponseEntity<Map<String, Object>> manageJobPositions(HttpServletRequest request, @RequestBody JobPositionRole jobPositionRole) {
		try {
			jobPositionService.manageJobPositions(jobPositionRole);

			User user = functionalitiesService.getUserFromHttpRequest(request);
			recentActivity.setMessage(" Role: " + jobPositionRole.getRole().getName() + " mapped job positions are updated.");
			recentActivity.setUser(user);
			recentActivityMapper.addRecentActivity(recentActivity);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Job positions mapped successfully");

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (Exception e) {
			logger.error("Error occurred during managing job position for role : {}", jobPositionRole.getRole().getCode(), e);
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Failed to map job positions");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@GetMapping("/selected_job_position")
	public ResponseEntity<List<JobPosition>> getRoleJobPositions(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(jobPositionService.getJobPositions(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// @GetMapping("/jobPositions")
	// public ResponseEntity<List<JobPosition>>
	// getMappedJobPositions(HttpServletRequest request) {
	// try {
	// return new ResponseEntity<>(jobPositionService.getMappedJobPositions(),
	// HttpStatus.OK);
	// } catch (Exception e) {
	//
	// return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	// }
	// }

	@GetMapping("/jobPositions")
	public ResponseEntity<List<JobPosition>> getMappedJobPositions(HttpServletRequest request) {
		return ResponseEntity.ok(jobPositionService.getMappedJobPositions());
	}

	@GetMapping("/total_job_positions")
	public ResponseEntity<List<JobPosition>> getTotalJobPositions(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(jobPositionService.getTotalJobPositions(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while fetching total job positions", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/job_positions_admin")
	public ResponseEntity<List<JobPosition>> getJobPositions(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(jobPositionService.getAllJobPositions(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
