package com.afr.fms.Auditor.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Service.AuditISMService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;

@RestController
@RequestMapping("/api/audit")
public class AuditISMController {

	@Autowired
	private AuditISMService auditService;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(AuditISMController.class);

	@PostMapping("/auditor/ism")
	public ResponseEntity<List<AuditISM>> getPassedAuditsForAuditor(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getPassedAuditsForAuditor(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching ISM findings for auditor: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/auditor/finding")
	public ResponseEntity<List<AuditISM>> checkFindingsExist(@RequestBody AuditISM finding,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditService.checkFindingsExist(finding), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error checking findings existence: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/auditor/rejected/ism/{auditor_id}")
	public ResponseEntity<List<AuditISM>> getRejectedFindingsForAuditor(@PathVariable Long auditor_id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getRejectedAuditsForAuditor(auditor_id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching rejected audits for auditor: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/auditor/drafting/ism")
	public ResponseEntity<List<AuditISM>> getAuditsOnDrafting(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getAuditsOnDrafting(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching drafted audits for auditor: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/auditor/progress/ism")
	public ResponseEntity<List<AuditISM>> getAuditsOnProgressForAuditor(@RequestBody User auditor,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getAuditsOnProgressForAuditor(auditor), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching audits on progress for auditor: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/ism")
	public ResponseEntity<HttpStatus> createISMFinding(@RequestBody AuditISM audit, HttpServletRequest request) {
		try {
			if (audit.getId() != null) {
				auditService.updateISMFinding(audit);
			} else {
				auditService.createISMFinding(audit);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error creating or updating ISM finding: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/ism/{id}")
	public ResponseEntity<HttpStatus> deleteISMFinding(@PathVariable("id") Long id,
			HttpServletRequest request) {
		try {
			auditService.deleteISMFinding(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error deleting ISM finding: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/selected/ism")
	public ResponseEntity<HttpStatus> deleteSelectedISMFinding(@RequestBody List<AuditISM> auditISMs,
			HttpServletRequest request) {
		try {
			auditService.deleteSelectedISMFinding(auditISMs);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error deleting selected ISM findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("pass/ism/{id}")
	public ResponseEntity<HttpStatus> passISMFinding(@PathVariable("id") Long id,
			HttpServletRequest request) {
		try {
			auditService.passISMFinding(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error passing ISM finding: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("pass/selected/ism")
	public ResponseEntity<HttpStatus> passSelectedISMFinding(@RequestBody List<AuditISM> auditISMs,
			HttpServletRequest request) {
		try {
			auditService.passSelectedISMFinding(auditISMs);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error passing selected ISM findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("back/ism/{id}")
	public ResponseEntity<HttpStatus> backISMFinding(@PathVariable("id") Long id,
			HttpServletRequest request) {
		try {
			auditService.backISMFinding(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error backing ISM finding: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("back/selected/ism")
	public ResponseEntity<HttpStatus> backSelectedISMFinding(@RequestBody List<AuditISM> auditISMs,
			HttpServletRequest request) {
		try {
			auditService.backSelectedISMFinding(auditISMs);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error backing selected ISM findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PutMapping("/ism")
	public ResponseEntity<HttpStatus> changeRecitificationStatus(@RequestBody AuditISM audit,
			HttpServletRequest request) {

		try {
			auditService.changeRecitificationStatus(audit);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error changing recitification status: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/ism")
	public ResponseEntity<List<AuditISM>> getAuditFindings(HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditService.getAuditFindings(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error fetching audit findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
