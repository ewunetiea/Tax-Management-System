package com.afr.fms.Reviewer.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Reviewer.Service.CommonActionReviewerService;

@RestController
@RequestMapping("/api/common-action/reviewer")
public class CommonActionReviewerController {
	@Autowired
	private CommonActionReviewerService commonActionReviewerService;
	@Autowired
	private RecentActivityMapper recentActivityMapper;

	RecentActivity recentActivity = new RecentActivity();

	@PutMapping("/review_finding/ism")
	public ResponseEntity<HttpStatus> reviewFindings(@RequestBody AuditISM audit, HttpServletRequest request) {

		try {
			commonActionReviewerService.reviewFindings(audit);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/cancel/ism")
	public ResponseEntity<HttpStatus> cancelFinding(@RequestBody Remark remark, HttpServletRequest request) {

		try {
			commonActionReviewerService.cancelFinding(remark);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/review_multi_finding/ism")
	public ResponseEntity<HttpStatus> multiReviewFindings(@RequestBody List<AuditISM> audit,
			HttpServletRequest request) {

		try {
			commonActionReviewerService.multiReviewFindings(audit);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// @PutMapping("/reviewer/cancel/selected/ism")
	// public ResponseEntity<HttpStatus> cancelSelectedFindings(@RequestBody
	// List<AuditISM> audits,
	// HttpServletRequest request) {
	// // if (functionalitiesService.verifyPermission(request, "get_ISM_findings"))
	// {
	// try {
	// for (AuditISM auditISM : audits) {
	// commonActionReviewerService.cancelFinding(auditISM);
	// }
	// return new ResponseEntity<>(HttpStatus.OK);

	// } catch (Exception ex) {
	// System.out.println(ex);
	// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// // } else {
	// // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	// // }
	// }

	@PutMapping("/unreview_finding/ism")
	public ResponseEntity<HttpStatus> unReviewFunding(@RequestBody AuditISM audit, HttpServletRequest request) {
		try {
			commonActionReviewerService.unReviewISM_Audit_Findings(audit);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/unreview_multi_finding/ism")

	public ResponseEntity<HttpStatus> unReview_Multi_Findings(@RequestBody List<AuditISM> audit,
			HttpServletRequest request) {

		try {
			commonActionReviewerService.unReview_Multi_Findings(audit);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
