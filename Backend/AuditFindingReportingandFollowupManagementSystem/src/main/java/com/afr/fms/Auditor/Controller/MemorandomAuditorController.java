package com.afr.fms.Auditor.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Service.MemorandomContingentService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@RestController
@RequestMapping("/api/memorandom")
public class MemorandomAuditorController {

	@Autowired
	private MemorandomContingentService memorandomContingentService;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(MemorandomAuditorController.class);

	@PostMapping("/auditor")
	public ResponseEntity<List<AuditISM>> getDraftedMemorandom(@RequestBody PaginatorPayLoad paginatorPayload,
			HttpServletRequest request) {

		return new ResponseEntity<>(memorandomContingentService.getFindingBasedOnStatus(paginatorPayload),
				HttpStatus.OK);

	}

	@PostMapping("/create-edit")
	public ResponseEntity<HttpStatus> createISMFinding(@RequestBody AuditISM audit, HttpServletRequest request) {
		if (audit.getId() != null) {
			memorandomContingentService.updateMemorandomAndConingent(audit);
		} else {
			memorandomContingentService.createMemorandomContingent(audit);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

}
