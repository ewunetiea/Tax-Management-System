package com.afr.fms.Common.Validation.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Admin.Entity.Awash_id;
import com.afr.fms.Admin.Entity.JobPosition;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Entity.UserCopyFromHR;
import com.afr.fms.Common.Validation.Service.UserValidationService;

@RestController
@RequestMapping("/api")

public class UserValidationController {
	@Autowired
	private UserValidationService userValidationService;

	@GetMapping("/checkUserEmail/{email}")
	public ResponseEntity<User> checkUserEmail(@PathVariable String email) {
		try {
			return new ResponseEntity<>(userValidationService.checkUserEmail(email), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/checkUserPhoneNumber/{phone_number}")
	public ResponseEntity<User> checkPhoneNumber(@PathVariable String phone_number) {
		try {
			return new ResponseEntity<>(userValidationService.checkPhoneNumber(phone_number), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// @PostMapping("/checkUserEmployeeId")
	// public ResponseEntity<UserCopyFromHR> checkUserEmployeeId(@RequestBody
	// Awash_id employee_id) {
	// try {

	// return new
	// ResponseEntity<>(userValidationService.checkUserEmployeeId(employee_id.getId_no(),
	// employee_id.getYear()), HttpStatus.OK);
	// } catch (Exception ex) {
	// return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	// }
	// }

	@PostMapping("/checkUserEmployeeId")
	public ResponseEntity<UserCopyFromHR> checkUserEmployeeId(@RequestBody Awash_id employee_id) {
		try {

			return new ResponseEntity<>(
					userValidationService.checkUserEmployeeId2(employee_id.getId_no(), employee_id.getYear()),
					HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/checkUserEmployeeIdSystem")
	public ResponseEntity<User> checkUserEmployeeIdSystem(@RequestBody Awash_id employee_id) {
		try {

			return new ResponseEntity<>(
					userValidationService.checkEmployeeIdSystem(employee_id.getId_no(), employee_id.getYear()),
					HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/fetchUserBranchAndPositionFromHrSystem")
	public ResponseEntity<User> fetchUserBranchAndPositionFromHrSystem(@RequestBody User user) {
		try {
			return new ResponseEntity<>(userValidationService.fetchUserBranchAndPositionFromHrSystem(user),
					HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/jobPosition/byRole/{id}")
	public ResponseEntity<String> checkJobPositionRole(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(userValidationService.checkJobPositionRole(id), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// checkJobPositionRole

	// getJobPositions

	// fetchUserBranchAndPositionFromHrSystem
}
