package com.afr.fms.Approver.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Approver.Service.CommonActionApproverService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@RestController
@RequestMapping("/api/common-action/approver")
public class CommonActionApproverController {

	@Autowired
	private CommonActionApproverService commonActionApproverService;

	RecentActivity recentActivity = new RecentActivity();

	@PutMapping("/approve")
	public ResponseEntity<HttpStatus> approveAudit(@RequestBody AuditISM audit, HttpServletRequest request) {
		try {
			commonActionApproverService.approveAudit(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/cancel")
	public ResponseEntity<HttpStatus> cancelApprovalISM_Audit(@RequestBody AuditISM audit, HttpServletRequest request) {
		try {
			commonActionApproverService.cancelApprovalISM_Audit(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/approve/selected")
	public ResponseEntity<HttpStatus> approveSelectedFindings(@RequestBody List<AuditISM> audits,
			HttpServletRequest request) {

		try {
			commonActionApproverService.approveSelectedFindings(audits);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/cancel/selected")
	public ResponseEntity<HttpStatus> cancelSelectedFindings(@RequestBody List<AuditISM> audits,
			HttpServletRequest request) {

		try {
			commonActionApproverService.cancelSelectedFindings(audits);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/reject")
	public ResponseEntity<HttpStatus> rejectFinding(@RequestBody Remark remark, HttpServletRequest request) {

		try {
			commonActionApproverService.rejectFinding(remark);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/approve/selected/email")
	public ResponseEntity<HttpStatus> approveandEmailSelectedFindings(@RequestBody List<AuditISM> audits,
			HttpServletRequest request) {
		try {
			commonActionApproverService.approveSelectedFindings(audits);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
