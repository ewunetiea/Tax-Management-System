package com.afr.fms.Division.Controller;

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
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Division.Service.AuditeeISMEnhancedService;

@RestController
@RequestMapping("/api/division_enhanced")
public class AuditeeISMEnhancedController {

	@Autowired
	private AuditeeISMEnhancedService auditeeISMService;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(AuditeeISMEnhancedController.class);

	@PostMapping("/partially_rectified/ism")
	public ResponseEntity<List<AuditISM>> getPartiallyRectifiedAuditsForDivision(
			@RequestBody PaginatorPayLoad paginatorPayLoad, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(
					auditeeISMService.getPartiallyRectifiedAuditsForAuditeeDivision(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching partially rectified audits for division: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/responded/ism/{division_id}")
	public ResponseEntity<List<AuditISM>> getRespondedAuditsForAuditeeDivision(@PathVariable Long division_id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getRespondedAuditsForAuditeeDivision(division_id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching responded audits for division: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/fetch")
	public ResponseEntity<List<AuditISM>> getAuditsForAuditeeDivision(@RequestBody PaginatorPayLoad paginatorPayLoad,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getAuditsForAuditeeDivision(paginatorPayLoad), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/progress/ism")
	public ResponseEntity<List<AuditISM>> getAuditsForAuditeeDivisionOnProgress(
			@RequestBody PaginatorPayLoad paginatorPayLoad, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getAuditsForAuditeeDivisionOnProgress(paginatorPayLoad),
					HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/unrectified/ism")
	public ResponseEntity<List<AuditISM>> getUnrectifiedAuditsForAuditeeDivision(
			@RequestBody PaginatorPayLoad paginatorPayLoad, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getUnrectifiedAuditsForAuditeeDivision(paginatorPayLoad),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching unrectified audits for division: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/response/ism/{auditee_division_id}")
	public ResponseEntity<AuditeeDivisionISM> getAuditeeResponseISM(@PathVariable Long auditee_division_id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getAuditeeResponseISM(auditee_division_id), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
