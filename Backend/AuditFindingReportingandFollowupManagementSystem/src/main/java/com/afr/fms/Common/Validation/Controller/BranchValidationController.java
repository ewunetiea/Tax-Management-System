package com.afr.fms.Common.Validation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Common.Validation.Service.BranchValidationService;

@RestController
@RequestMapping("/api")
public class BranchValidationController {
	@Autowired
	private BranchValidationService branchValidationService;

	@GetMapping("/branch/name/{name}")
	public ResponseEntity<Branch> checkBranchName(@PathVariable String name) {
		try {
			return new ResponseEntity<>(branchValidationService.checkBranchName(name), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/branch/code/{code}")
	public ResponseEntity<Branch> checkBranchCode(@PathVariable String code) {
		try {
			return new ResponseEntity<>(branchValidationService.checkBranchCode(code), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}