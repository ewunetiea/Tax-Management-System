package com.tms.Common.Validation.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.Admin.Entity.Awash_id;
import com.tms.Admin.Entity.User;
import com.tms.Admin.Entity.UserCopyFromHR;
import com.tms.Common.Validation.Service.UserValidationService;

@RestController
@RequestMapping("/api")

public class UserValidationController {
	@Autowired
	private UserValidationService userValidationService;

	private static final Logger logger = LoggerFactory.getLogger(UserValidationController.class);

	@GetMapping("/checkUserEmail/{email}")
	public ResponseEntity<User> checkUserEmail(@PathVariable String email) {
		try {
			return new ResponseEntity<>(userValidationService.checkUserEmail(email), HttpStatus.OK);
		} catch (Exception ex) {
			logger.info("Error while checking email:  ", ex);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/checkUsername/{username}")
	public ResponseEntity<User> checkUsername(@PathVariable String username) {
		try {
			return new ResponseEntity<>(userValidationService.checkUsername(username), HttpStatus.OK);
		} catch (Exception ex) {
			logger.info("Error while checking username:  ", ex);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/checkUserPhoneNumber/{phone_number}")
	public ResponseEntity<User> checkPhoneNumber(@PathVariable String phone_number) {
		try {
			return new ResponseEntity<>(userValidationService.checkPhoneNumber(phone_number), HttpStatus.OK);
		} catch (Exception ex) {
			logger.info("Error while checking phone:  ", ex);
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
			logger.info("Error while checking employee id:  ", ex);
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
			logger.info("Error while checking employee id:  ", ex);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/jobPosition/byRole/{id}")
	public ResponseEntity<String> checkJobPositionRole(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(userValidationService.checkJobPositionRole(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.info("Error while checking job position role:  ", ex);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
