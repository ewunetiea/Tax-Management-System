package com.afr.fms.Approver.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.afr.fms.Approver.Service.CashCountApproverService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/approver/cash_count_audit")
public class CashCountApproverController {

	@Autowired
	private CashCountApproverService cashCountApproverService;

	private static final Logger logger = LoggerFactory.getLogger(CashCountApproverController.class);

	@PostMapping("/pending")
	public ResponseEntity<List<AuditISM>> getPendingAudits(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			List<AuditISM> audits = cashCountApproverService.getPendingAudits(paginatorPayLoad);

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
			List<AuditISM> audits = cashCountApproverService.getApprovedAudits(paginatorPayLoad);

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
			List<AuditISM> audits = cashCountApproverService.getRejectedAudits(paginatorPayLoad);

			return new ResponseEntity<>(audits, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching pending audits for a user ID: {} - {}", paginatorPayLoad.getUser_id(),
					ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
