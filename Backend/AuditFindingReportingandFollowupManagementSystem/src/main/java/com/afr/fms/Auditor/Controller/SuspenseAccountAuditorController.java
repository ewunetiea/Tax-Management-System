package com.afr.fms.Auditor.Controller;

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
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Service.AuditISMService;
import com.afr.fms.Auditor.Service.SuspenseAccountService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@RestController
@RequestMapping("/api/suspense-account")
public class SuspenseAccountAuditorController {
	@Autowired
	private AuditISMService auditService;

	@Autowired
	private SuspenseAccountService suspenseAccountService;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(SuspenseAccountAuditorController.class);

	@GetMapping("/auditor/rejected/{auditor_id}")
	public ResponseEntity<List<AuditISM>> getRejectedFindingsForAuditor(@PathVariable Long auditor_id,
			HttpServletRequest request) {
		return new ResponseEntity<>(auditService.getRejectedAuditsForAuditor(auditor_id), HttpStatus.OK);

	}

	@PostMapping("/auditor")
	public ResponseEntity<List<AuditISM>> getAuditsOnDrafting(@RequestBody PaginatorPayLoad paginatorPayload,
			HttpServletRequest request) {
		return new ResponseEntity<>(suspenseAccountService.getFindingBasedOnStatus(paginatorPayload), HttpStatus.OK);

	}

	@PostMapping("/create-edit")
	public ResponseEntity<HttpStatus> createISMFinding(@RequestBody AuditISM audit, HttpServletRequest request) {
		if (audit.getId() != null) {
			suspenseAccountService.updateSuspenseAccount(audit);
		} else {
			suspenseAccountService.createSuspenseAccount(audit);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

}
