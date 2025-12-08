package com.tms.Common.RecentActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.Admin.Entity.User;
import com.tms.Common.Entity.Report;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class RecentActivityController {

	@Autowired
	private RecentActivityService recentActivityService;

	private static final Logger logger = LoggerFactory.getLogger(RecentActivityController.class);

	@PostMapping("/addRecentActivity")
	public ResponseEntity<HttpStatus> addRecentActivity(@RequestBody RecentActivity ra, HttpServletRequest request) {
		try {
			recentActivityService.addRecentActivity(ra);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while adding recent activity: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/getRecentActivityAdmin")
	public ResponseEntity<List<RecentActivity>> getRecentActivityAdmin(@RequestBody Report report,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(recentActivityService.generateRecentActivities(report), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching recent activity by admin: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/get_all_Activity")
	public ResponseEntity<List<RecentActivity>> getAllRecentActivities(@RequestBody RecentActivity recentActivity,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(recentActivityService.getAllRecentActivities(recentActivity), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching recent activity: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/users_activity")
	public ResponseEntity<List<User>> getUsers(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(recentActivityService.getUsers(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching users for recent activity: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getRecentActivity/{id}")
	public ResponseEntity<List<RecentActivity>> getRecentActivityByUserId(@PathVariable("id") Long id) {
		try {
			List<RecentActivity> activities = recentActivityService.getAllRecentActivityByUserId(id);
			return ResponseEntity.ok(activities); 
		} catch (Exception e) {
			logger.error("Error while fetching recent activity by user id: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
		}
	}



		@PostMapping("/getActivity")
	public ResponseEntity<List<RecentActivity>> getRecentActivityByContentAndDate(@RequestBody Report report,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(recentActivityService.getRecentActivityByContentAndDate(report), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching recent activity by admin: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
