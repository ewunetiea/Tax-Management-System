package com.afr.fms.Admin.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import com.afr.fms.Admin.Entity.JobPosition;
import com.afr.fms.Admin.Entity.JobPositionRole;
import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Service.JobPositionService;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

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
	public ResponseEntity<?> manageJobPositions(HttpServletRequest request, @RequestBody JobPositionRole jobPositionRole) {
		System.out.println("Ffffffffffffffffffffffffffffffffffffff Managing job positions for role: " + jobPositionRole.getRole().getName());
		try {
			jobPositionService.manageJobPositions(jobPositionRole);
			User user = functionalitiesService.getUserFromHttpRequest(request);
			recentActivity.setMessage(" Role: " + jobPositionRole.getRole().getName() + " mapped job positions are updated.");
			recentActivity.setUser(user);
			recentActivityMapper.addRecentActivity(recentActivity);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error occurred during managing job position for role : {}", jobPositionRole.getRole().getCode(), e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/selected_job_position")
	public ResponseEntity<List<JobPosition>> getRoleJobPositions(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(jobPositionService.getJobPositions(), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/jobPositions")
	public ResponseEntity<List<JobPosition>> getMappedJobPositions(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(jobPositionService.getMappedJobPositions(), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/total_job_positions")
	public ResponseEntity<List<JobPosition>> getTotalJobPositions(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(jobPositionService.getTotalJobPositions(), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/job_positions_admin")
	public ResponseEntity<List<JobPosition>> getJobPositions(HttpServletRequest request) {

		try {
			return new ResponseEntity<>(jobPositionService.getAllJobPositions(), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

}
