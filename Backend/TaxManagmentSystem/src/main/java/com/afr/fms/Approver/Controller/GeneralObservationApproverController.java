package com.afr.fms.Approver.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.afr.fms.Approver.Service.GeneralObservationApproverService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@RestController
@RequestMapping("/api/approver/generalObservation")
public class GeneralObservationApproverController {
	@Autowired
	private GeneralObservationApproverService generalObservationApproverService;

	private static final Logger logger = LoggerFactory.getLogger(GeneralObservationApproverController.class);

	@PostMapping("/pending")
	public ResponseEntity<List<AuditISM>> getPendingAudits(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			List<AuditISM> audits = generalObservationApproverService.getPendingAudits(paginatorPayLoad);

			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching pending audits for a user ID: {} - {}", paginatorPayLoad.getUser_id(),
					ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/approved")
	public ResponseEntity<List<AuditISM>> getApprovedAudits(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			List<AuditISM> audits = generalObservationApproverService.getApprovedAudits(paginatorPayLoad);

			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching approved audits for a user ID: {} - {}", paginatorPayLoad.getUser_id(),
					ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/rejected")
	public ResponseEntity<List<AuditISM>> getRejectedAudits(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			List<AuditISM> audits = generalObservationApproverService.getRejectedAudits(paginatorPayLoad);

			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching pending audits for a user ID: {} - {}", paginatorPayLoad.getUser_id(),
					ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
