package com.afr.fms.Followup_officer.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Followup_officer.Service.FollowupOfficerMGTEnhancedService;

@RestController
@RequestMapping("/api/audit/followup_officer_enhanced")
public class FollowupOfficerMGTEnhancedController {

	@Autowired
	private FollowupOfficerMGTEnhancedService auditService;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(FollowupOfficerMGTEnhancedController.class);

	@PostMapping("/ism")
	public ResponseEntity<List<AuditISM>> getAuditsForFollowupOfficer(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getAuditsForFollowupOfficer(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching audits for followup officer: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/rectified/ism")
	public ResponseEntity<List<AuditISM>> getRectifiedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getRectifiedFindings(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching rectified findings: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/unrectified/ism")
	public ResponseEntity<List<AuditISM>> getUnrectifiedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getUnrectifiedFindings(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching unrectified findings: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/rejected/ism")
	public ResponseEntity<List<AuditISM>> getRejectedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getRejectedFindings(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching rejected findings: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/partially_rectified/ism")
	public ResponseEntity<List<AuditISM>> getPartiallyRectifiedFindings(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getPartiallyRectifiedFindings(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching partially rectified findings: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
