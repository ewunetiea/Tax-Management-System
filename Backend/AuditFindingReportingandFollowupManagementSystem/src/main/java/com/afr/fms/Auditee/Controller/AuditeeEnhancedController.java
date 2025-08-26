package com.afr.fms.Auditee.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import com.afr.fms.Auditee.Service.AuditeeEnhancedService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;

@RestController
@RequestMapping("/api/auditee_enhanced")
public class AuditeeEnhancedController {

	@Autowired
	private AuditeeEnhancedService auditeeISMService;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(AuditeeEnhancedController.class);

	@PostMapping("/fetch")
	public ResponseEntity<List<AuditISM>> getAuditsForAuditee(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getAuditsForAuditee(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching findings in auditee side: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/partially_rectified/ism")
	public ResponseEntity<List<AuditISM>> getPartiallyRectifiedAuditsForAuditee(
			@RequestBody PaginatorPayLoad paginatorPayLoad, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getPartiallyRectifiedAuditsForAuditee(paginatorPayLoad),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching get_partially_rectified_audits_for_auditee_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/progress/ism")
	public ResponseEntity<List<AuditISM>> getAuditsForAuditeeOnProgress(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getAuditsForAuditeeOnProgress(paginatorPayLoad),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching get_audits_for_auditee_on_progress_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/unrectified/ism")
	public ResponseEntity<List<AuditISM>> getUnrectifiedAuditsForAuditee(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getUnrectifiedAuditsForAuditee(paginatorPayLoad),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching get_unrectified_audits_for_auditee_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/responded/ism/{auditee_id}")
	public ResponseEntity<List<AuditISM>> getRespondedAuditsForAuditee(@PathVariable Long auditee_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditeeISMService.getRespondedAuditsForAuditee(auditee_id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching get_responded_audits_for_auditee_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
