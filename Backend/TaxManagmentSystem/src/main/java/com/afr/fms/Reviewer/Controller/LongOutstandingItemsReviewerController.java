package com.afr.fms.Reviewer.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Reviewer.Service.LongOutstandingItemsReviewerService;

@RestController
@RequestMapping("/api/reviewer/longOutstandingItems")
public class LongOutstandingItemsReviewerController {

	@Autowired
	private LongOutstandingItemsReviewerService longOutstandingItemsReviewerService;

	private static final Logger logger = LoggerFactory.getLogger(LongOutstandingItemsReviewerController.class);

	@PostMapping("/pending")
	public ResponseEntity<List<AuditISM>> getPendingFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			List<AuditISM> audits = longOutstandingItemsReviewerService.getPendingFindings(paginatorPayLoad);
			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching pending audits for auditor ID: {} - {}", paginatorPayLoad.getUser_id(),
					ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/reviewed")
	public ResponseEntity<List<AuditISM>> getReviewedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			List<AuditISM> audits = longOutstandingItemsReviewerService.getReviewedFindings(paginatorPayLoad);
			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching reviewed audits for auditor ID: {} - {}", paginatorPayLoad.getUser_id(),
					ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/rejected")
	public ResponseEntity<List<AuditISM>> geRejectedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			List<AuditISM> audits = longOutstandingItemsReviewerService.geRejectedFindings(paginatorPayLoad);
			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching rejected audits for auditor ID: {} - {}", paginatorPayLoad.getUser_id(),
					ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
