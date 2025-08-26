package com.afr.fms.Reviewer.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Reviewer.Service.CashCountReviewerService;

@RestController
@RequestMapping("/api/reviewer/cash_count_audit")
public class CashCountReviewerController {

    @Autowired
    private CashCountReviewerService cashCountReviewerService;

    private static final Logger logger = LoggerFactory.getLogger(CashCountReviewerController.class);

     @PostMapping("/pending")
	public ResponseEntity<List<AuditISM>> getPendingAudits(@RequestBody  PaginatorPayLoad paginatorPayLoad, HttpServletRequest request) {
		try {
			List<AuditISM> audits = cashCountReviewerService.getPendingAudits(paginatorPayLoad);
			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching pending audits for a user ID: {} - {}",  paginatorPayLoad.getUser_id(), ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @PostMapping("/reviewed")
	public ResponseEntity<List<AuditISM>> getReviewedAudits(@RequestBody PaginatorPayLoad paginatorPayLoad, HttpServletRequest request) {
		try {
			List<AuditISM> audits = cashCountReviewerService.getReviewedAudits(paginatorPayLoad);
			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching drafted audits for user ID: {} - {}",  paginatorPayLoad.getUser_id(), ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PostMapping("/rejected")
	public ResponseEntity<List<AuditISM>> getRejectedAudits(@RequestBody PaginatorPayLoad paginatorPayLoad, HttpServletRequest request) {
		try {
			List<AuditISM> audits = cashCountReviewerService.getRejectedAudits(paginatorPayLoad);
			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching rejected audits for user ID: {} - {}",  paginatorPayLoad.getUser_id(), ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
}
