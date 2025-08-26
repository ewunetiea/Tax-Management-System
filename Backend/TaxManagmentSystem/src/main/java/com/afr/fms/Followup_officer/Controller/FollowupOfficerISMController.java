package com.afr.fms.Followup_officer.Controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Followup_officer.Service.FollowupOfficerISMService;

@RestController
@RequestMapping("/api/audit/followup_officer")
public class FollowupOfficerISMController {

	@Autowired
	private FollowupOfficerISMService auditService;

	private static final Logger logger = LoggerFactory.getLogger(FollowupOfficerISMController.class);

	@PostMapping("/ism")
	public ResponseEntity<List<AuditISM>> getAuditsForFollowupOfficer(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getAuditsForFollowupOfficer(auditISM), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("get_pending_findings_followup_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/rectified/ism")
	public ResponseEntity<List<AuditISM>> getRectifiedFindings(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getRectifiedFindings(auditISM), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("get_rectified_findings_followup_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/unrectified/ism")
	public ResponseEntity<List<AuditISM>> getUnrectifiedFindings(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getUnrectifiedFindings(auditISM), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("get_unrectified_findings_followup_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/rejected/ism/{id}")
	public ResponseEntity<List<AuditISM>> getRejectedFindings(@PathVariable Long id, HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getRejectedFindings(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("get_rejected_findings_followup_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/partially_rectified/ism")
	public ResponseEntity<List<AuditISM>> getPartiallyRectifiedFindings(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getPartiallyRectifiedFindings(auditISM), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("get_partially_rectified_findings_followup_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/rectify/ism")
	public ResponseEntity<HttpStatus> rectifyFinding(@RequestBody AuditISM audit, HttpServletRequest request) {
		try {
			auditService.rectifyISMFindings(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("rectify_finding_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/rectify_multi_audit/ism")
	public ResponseEntity<HttpStatus> rectifyMultipleFindings(@RequestBody List<AuditISM> audit,
			HttpServletRequest request) {
		try {
			auditService.rectifyMultipleAudits(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("rectify_finding_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/unRectify/ism")
	public ResponseEntity<HttpStatus> unRectifyISMFindings(@RequestBody AuditISM audit,
			HttpServletRequest request) {
		try {
			auditService.unRectifyISMFindings(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("un_rectify_finding_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/reject/ism")
	public ResponseEntity<HttpStatus> rejectFinding(@RequestBody Remark remark, HttpServletRequest request) {
		try {
			auditService.rejectISMFinding(remark);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("reject_finding_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/unrectify_multi_audit/ism")
	public ResponseEntity<HttpStatus> unRectifySelectedISMFindings(@RequestBody List<AuditISM> audit,
			HttpServletRequest request) {
		try {

			auditService.unRectifySelectedISMFindings(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("unrectify_finding_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/partially_rectify/ism")
	public ResponseEntity<HttpStatus> partiallyRectifyISMFindings(@RequestBody Remark remark,
			HttpServletRequest request) {
		try {
			auditService.partiallyRectifyISMFindings(remark);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("partially_rectify_finding_is_mgt ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
