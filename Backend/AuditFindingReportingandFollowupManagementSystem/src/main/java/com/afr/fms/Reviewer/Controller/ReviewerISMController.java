package com.afr.fms.Reviewer.Controller;

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

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Reviewer.Service.ReviewerISMService;


@RestController
@RequestMapping("/api/audit/reviewer")
public class ReviewerISMController {

	@Autowired
	private ReviewerISMService auditService;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(ReviewerISMController.class);

	@PostMapping("/ism")
	public ResponseEntity<List<AuditISM>> getAuditsForReviewer(@RequestBody User user,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getAuditsForReviewer(user), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching ISM findings for reviewer: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/reviewed/ism/{reviewer_id}")
	public ResponseEntity<List<AuditISM>> getReviewed_Findings(@PathVariable Long reviewer_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getReviewedFindings(reviewer_id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching reviewed ISM findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/approver_rejected/ism/{reviewer_id}")
	public ResponseEntity<List<AuditISM>> getApproverRejectedFindings(@PathVariable Long reviewer_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getApproverRejectedFindings(reviewer_id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching approver rejected ISM findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/rejected/ism/{reviewer_id}")
	public ResponseEntity<List<AuditISM>> getRejectedFindings(@PathVariable Long reviewer_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getRejectedFindings(reviewer_id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching rejected ISM findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/reviewedStatus/ism/{reviewer_id}")
	public ResponseEntity<List<AuditISM>> getReviewedFindingsStatus(@PathVariable Long reviewer_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.getReviewedFindingsStatus(reviewer_id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching reviewed findings status: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/review_finding/ism")
	public ResponseEntity<HttpStatus> reviewFindings(@RequestBody AuditISM audit, HttpServletRequest request) {

		try {
			auditService.reviewFindings(audit);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error reviewing ISM finding: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/cancel/ism")
	public ResponseEntity<HttpStatus> cancelFinding(@RequestBody Remark remark, HttpServletRequest request) {

		try {
			auditService.cancelFinding(remark);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error canceling ISM finding: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/review_multi_finding/ism")
	public ResponseEntity<HttpStatus> multiReviewFindings(@RequestBody List<AuditISM> audit,
			HttpServletRequest request) {
		try {
			auditService.multiReviewFindings(audit);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error reviewing multiple ISM findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/unreview_finding/ism")
	public ResponseEntity<HttpStatus> unReviewFunding(@RequestBody AuditISM audit, HttpServletRequest request) {
		try {
			auditService.unReviewISM_Audit_Findings(audit);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error unreviewing ISM finding: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/unreview_multi_finding/ism")

	public ResponseEntity<HttpStatus> unReview_Multi_Findings(@RequestBody List<AuditISM> audit,
			HttpServletRequest request) {
		try {
			auditService.unReview_Multi_Findings(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error unreviewing multiple ISM findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
