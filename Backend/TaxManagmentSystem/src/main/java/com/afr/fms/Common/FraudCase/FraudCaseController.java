package com.afr.fms.Common.FraudCase;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Payload.endpoint.Endpoint;

@RestController
@RequestMapping("/api/fraudcase")
public class FraudCaseController {

	@Autowired
	FraudCaseService fraudCaseService;

	@PostMapping("/create")
	public ResponseEntity<HttpStatus> createFraudCase(@RequestBody FraudCase fraudcase, HttpServletRequest request) {
		try {

			if (fraudcase.getId() != null && fraudcase.getCreated_date() != null) {
				fraudCaseService.updateFraudCase(fraudcase);
			} else {
				fraudCaseService.createFraudCase(fraudcase);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/auditor/initial/{initial}")
	public ResponseEntity<FraudCase> getInitialFraudCase(HttpServletRequest request, @PathVariable Long initial) {

		try {

			return new ResponseEntity<>(fraudCaseService.getInitialFraudCase(initial), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/auditor/drafted/{auditor_id}")
	public ResponseEntity<List<FraudCase>> getFraudCases(HttpServletRequest request, @PathVariable Long auditor_id) {
		try {

			return new ResponseEntity<>(fraudCaseService.getDraftedFraudAuditor(auditor_id), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/auditor/passed/{auditor_id}")
	public ResponseEntity<List<FraudCase>> getPassedFraudAuditor(HttpServletRequest request,
			@PathVariable Long auditor_id) {
		try {

			return new ResponseEntity<>(fraudCaseService.getPassedFraudAuditor(auditor_id), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/auditor/approved/{auditor_id}")
	public ResponseEntity<List<FraudCase>> getApprovedFraudAuditor(HttpServletRequest request,
			@PathVariable Long auditor_id) {
		try {

			return new ResponseEntity<>(fraudCaseService.getApprovedFraudAuditor(auditor_id), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/auditor/pass/{auditor_id}")
	public ResponseEntity<List<HttpStatus>> passFraudCase(HttpServletRequest request, @PathVariable Long auditor_id) {
		try {
			fraudCaseService.passFraudCase(auditor_id);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/auditor/back/{id}")
	public ResponseEntity<HttpStatus> backFraudCase(HttpServletRequest request, @PathVariable Long id) {
		try {
			fraudCaseService.passFraudCase(id);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/approver/approve")
	public ResponseEntity<HttpStatus> approveFraudCase(@RequestBody FraudCase fraudCase, HttpServletRequest request) {
		try {

			fraudCaseService.approveFraudCase(fraudCase);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		

	}

	@GetMapping("approver/cancel/{id}")
	public ResponseEntity<HttpStatus> cancelApprovedFraudCase(@PathVariable Long id,
			HttpServletRequest request) {
		
		try {

			fraudCaseService.cancelApprovedFraudCase(id);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		

	}

	@GetMapping("/approver/pending")
	public ResponseEntity<List<FraudCase>> getPendingFraudCase(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(fraudCaseService.getPendingFraudCasesApprover(), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@GetMapping("/approver/approved/{approver_id}")
	public ResponseEntity<List<FraudCase>> getApprovedFraudCase(HttpServletRequest request,
			@PathVariable Long approver_id) {
		
		try {

			return new ResponseEntity<>(fraudCaseService.getApprovedFraudCasesApprover(approver_id), HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@DeleteMapping("/auditor/delete/{id}")
	public ResponseEntity<HttpStatus> deleteFraudCase(HttpServletRequest request, @PathVariable Long id) {

		try {
			fraudCaseService.deleteFraudCase(id);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
