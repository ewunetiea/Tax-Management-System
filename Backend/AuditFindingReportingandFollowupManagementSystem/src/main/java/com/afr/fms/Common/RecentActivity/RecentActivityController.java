package com.afr.fms.Common.RecentActivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Common.Permission.Service.FunctionalitiesService;

import com.afr.fms.Common.Entity.Report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
// @PreAuthorize("hasAnyRole('MAKER','APPROVER','ADMIN','HO')")
public class RecentActivityController {

	@Autowired
	private RecentActivityService recentActivityService;

	@Autowired
	private FunctionalitiesService functionalitiesService;

	@PostMapping("/addRecentActivity")
	public ResponseEntity<HttpStatus> addRecentActivity(@RequestBody RecentActivity ra, HttpServletRequest request) {
		try {
			recentActivityService.addRecentActivity(ra);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getRecentActivity/{id}")
	public ResponseEntity<List<RecentActivity>> getRecentActivity(@PathVariable Long id, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(recentActivityService.getRecentActivityByUserId(id), HttpStatus.OK);
		} catch (Exception ex) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/getRecentActivityAdmin")
	public ResponseEntity<List<RecentActivity>> getRecentActivityAdmin(@RequestBody Report report,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(recentActivityService.getRecentActivityAdmin(report), HttpStatus.OK);
		} catch (Exception ex) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/getAllRecentActivityByUserId")
	public ResponseEntity<List<RecentActivity>> getAllRecentActivityByUserId(@RequestBody Report report,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(recentActivityService.getAllRecentActivityByUserId(report), HttpStatus.OK);
		} catch (Exception ex) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/getActivity")
	public ResponseEntity<List<RecentActivity>> getActivityByDateAndContent(@RequestBody Report report,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(recentActivityService.getActivityByDateAndContent(report), HttpStatus.OK);
		} catch (Exception ex) {

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
