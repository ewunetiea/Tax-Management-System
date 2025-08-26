package com.afr.fms.Reviewer.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Reviewer.Entity.ReviewerDashBoard;
import com.afr.fms.Reviewer.Service.DashboardService;


@RestController
@RequestMapping("/api/dashboard")

public class DashboardController {
	@Autowired
	private DashboardService dashboardService;

	@PostMapping("/reviewer/ism/perdirectorate")
	public ResponseEntity<ReviewerDashBoard> getDashboardData(@RequestBody User user, HttpServletRequest request) {

		try {
			return new ResponseEntity<>(dashboardService.getDashboardData(user), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
