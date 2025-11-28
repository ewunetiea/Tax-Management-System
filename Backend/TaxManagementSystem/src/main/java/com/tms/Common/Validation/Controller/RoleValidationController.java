package com.tms.Common.Validation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.Admin.Entity.Role;
import com.tms.Common.Validation.Service.RoleValidationService;
import com.tms.Payload.endpoint.Endpoint;


@RestController
@RequestMapping("/api")

public class RoleValidationController {

	@Autowired
	private RoleValidationService roleValidationService;

	@GetMapping("/role/code/{code}")
	public ResponseEntity<Role> checkRoleCode(@PathVariable String code) {
		try {
			return new ResponseEntity<>(roleValidationService.checkRoleCode(code), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/role/name/{name}")
	public ResponseEntity<Role> checkRoleName(@PathVariable String name) {
		try {
			return new ResponseEntity<>(roleValidationService.checkRoleName(name), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
