package com.afr.fms.Approver.Controller;

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

import com.afr.fms.Approver.Service.AuditISMApproverService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Audit_Remark.Remark;

@RestController
@RequestMapping("/api/audit/approver")
public class AuditISMApproverController {

	@Autowired
	private AuditISMApproverService auditService;

	private static final Logger logger = LoggerFactory.getLogger(AuditISMApproverController.class);

	@PostMapping("/ism/approved")
	public ResponseEntity<List<AuditISM>> getApprovedFindings(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getApprovedFindings(auditISM), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching approved findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/ism/rejected/{id}")
	public ResponseEntity<List<AuditISM>> getRejectedFindings(@PathVariable Long id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getRejectedFindings(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching rejected findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/ism/approvedStatus/{approver_id}")
	public ResponseEntity<List<AuditISM>> getApprovedFindingsStatus(@PathVariable Long approver_id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getApprovedFindingsStatus(approver_id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching approved findings status: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/ism/{category}")
	public ResponseEntity<List<AuditISM>> getAuditsForApprover(@PathVariable String category,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getAuditsForApprover(category), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching audits for approver: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/ism")
	public ResponseEntity<HttpStatus> approveAudit(@RequestBody AuditISM audit, HttpServletRequest request) {
		try {
			auditService.approveAudit(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error approving audit: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/cancel/ism")
	public ResponseEntity<HttpStatus> cancelApprovalISM_Audit(@RequestBody AuditISM audit, HttpServletRequest request) {

		try {
			auditService.cancelApprovalISM_Audit(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error canceling approval: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/all/ism")
	public ResponseEntity<HttpStatus> approveSelectedFindings(@RequestBody List<AuditISM> audits,
			HttpServletRequest request) {

		try {
			auditService.approveSelectedFindings(audits);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error approving selected findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/rectified/ism")
	public ResponseEntity<HttpStatus> approveSelectedRectifiedFindings(@RequestBody Remark remark,
			HttpServletRequest request) {

		try {
			auditService.approveSelectedRectifiedFindings(remark);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error approving selected rectified findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/cancel/all/ism")
	public ResponseEntity<HttpStatus> cancelSelectedFindings(@RequestBody List<AuditISM> audits,
			HttpServletRequest request) {

		try {
			auditService.cancelSelectedFindings(audits);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error canceling selected findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/reject/ism")
	public ResponseEntity<HttpStatus> rejectFinding(@RequestBody Remark remark, HttpServletRequest request) {

		try {
			auditService.rejectFinding(remark);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error rejecting finding: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
