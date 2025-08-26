package com.afr.fms.Common.Report.ResponseAge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.Report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ism/report")
public class AuditeeResponseAgeController {

	@Autowired
	private AuditeeResponseAgeService auditeeResponseAgeService;



	
	@PostMapping("/auditee_response_age")
	public ResponseEntity<List<AuditISM>> getAuditeeResponseAgeReport(@RequestBody Report report,
			HttpServletRequest request) {
			try {
				return new ResponseEntity<>(auditeeResponseAgeService.getReport(report), HttpStatus.OK);
			} catch (Exception ex) {
				System.out.println(ex);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
	}

}
