package com.afr.fms.Auditor.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Service.CommonActionAuditorService;
import com.afr.fms.Common.RecentActivity.RecentActivity;

@RestController
@RequestMapping("/api/common-action/auditor")
public class CommonActionAuditorController {

	@Autowired
	private CommonActionAuditorService commonActionAuditorService;

	RecentActivity recentActivity = new RecentActivity();

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteISMFinding(@PathVariable("id") Long id, HttpServletRequest request) {
		commonActionAuditorService.deleteISMFinding(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PostMapping("/delete/selected")
	public ResponseEntity<HttpStatus> deleteSelectedISMFinding(@RequestBody List<AuditISM> auditISMs,
			HttpServletRequest request) {
		commonActionAuditorService.deleteSelectedISMFinding(auditISMs);
		return new ResponseEntity<>(null, HttpStatus.OK);

	}

	@GetMapping("pass/{id}")
	public ResponseEntity<HttpStatus> passISMFinding(@PathVariable("id") Long id, HttpServletRequest request) {
		commonActionAuditorService.passISMFinding(id);
		return new ResponseEntity<>(null, HttpStatus.OK);

	}

	@PostMapping("pass/selected")
	public ResponseEntity<HttpStatus> passSelectedISMFinding(@RequestBody List<AuditISM> auditISMs,
			HttpServletRequest request) {
		commonActionAuditorService.passSelectedISMFinding(auditISMs);
		return new ResponseEntity<>(null, HttpStatus.OK);

	}

	@GetMapping("back/{id}")
	public ResponseEntity<HttpStatus> backISMFinding(@PathVariable("id") Long id, HttpServletRequest request) {
		commonActionAuditorService.backISMFinding(id);
		return new ResponseEntity<>(null, HttpStatus.OK);

	}

	@PostMapping("back/selected")
	public ResponseEntity<HttpStatus> backSelectedISMFinding(@RequestBody List<AuditISM> auditISMs,
			HttpServletRequest request) {
		commonActionAuditorService.backSelectedISMFinding(auditISMs);
		return new ResponseEntity<>(null, HttpStatus.OK);

	}

	@PutMapping("/rectification")
	public ResponseEntity<HttpStatus> changeRecitificationStatus(@RequestBody AuditISM audit,
			HttpServletRequest request) {

		commonActionAuditorService.changeRecitificationStatus(audit);
		return new ResponseEntity<>(HttpStatus.OK);

	}

}
