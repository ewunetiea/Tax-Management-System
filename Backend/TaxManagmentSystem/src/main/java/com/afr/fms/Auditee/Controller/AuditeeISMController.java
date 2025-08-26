package com.afr.fms.Auditee.Controller;

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
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditee.Service.AuditeeISMService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Division.Service.AuditeeISMEnhancedService;

@RestController
@RequestMapping("/api/division")
public class AuditeeISMController {

	@Autowired
	private AuditeeISMService auditeeISMService;

	@Autowired
	private AuditeeISMEnhancedService auditeeISMEnhancedService;

	@Autowired
	private AuditISMMapper auditISMMapper;

	@Autowired
	private RecentActivityMapper recentActivityMapper;

	@Autowired
	private FunctionalitiesService functionalitiesService;
	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(AuditeeISMController.class);

	@PostMapping("/ism")
	public ResponseEntity<HttpStatus> add_response(@RequestBody AuditeeDivisionISM auditee_response,
			HttpServletRequest request) {
		try {
			auditeeISMService.add_response(auditee_response);
			User user = functionalitiesService.getUserFromHttpRequest(request);
			AuditISM auditISM = auditISMMapper.getAudit(auditee_response.getAudit_id());
			recentActivity.setMessage(" Auditee Response for audit " + auditISM.getCase_number() + " is added ");
			recentActivity.setUser(user);
			recentActivityMapper.addRecentActivity(recentActivity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while adding auditee response: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/partially_rectified/ism/{auditee_id}")
	public ResponseEntity<List<AuditISM>> getPartiallyRectifiedAuditsForDivision(@PathVariable Long auditee_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditeeISMService.getPartiallyRectifiedAuditsForAuditeeDivision(auditee_id),
					HttpStatus.OK);
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

	@GetMapping("/ism/{division_id}")
	public ResponseEntity<List<AuditISM>> getAuditsForAuditeeDivision(@PathVariable Long division_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditeeISMService.getAuditsForAuditeeDivision(division_id), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/progress/ism/{division_id}")
	public ResponseEntity<List<AuditISM>> getAuditsForAuditeeDivisionOnProgress(@PathVariable Long division_id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getAuditsForAuditeeDivisionOnProgress(division_id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching audits for division on progress: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/unrectified/ism/{division_id}")
	public ResponseEntity<List<AuditISM>> getUnrectifiedAuditsForAuditeeDivision(@PathVariable Long division_id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getUnrectifiedAuditsForAuditeeDivision(division_id),
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
			return new ResponseEntity<>(auditeeISMService.getAuditeeResponseISM(auditee_division_id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching auditee response for ISM: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/submit/ism")
	public ResponseEntity<HttpStatus> submitISMFinding(@RequestBody AuditISM auditISM, HttpServletRequest request) {
		try {
			auditeeISMService.submitISMFinding(auditISM);
			User user = functionalitiesService.getUserFromHttpRequest(request);
			// AuditISM auditISM = auditISMMapper.getAudit(auditISM.getId());
			recentActivity.setMessage(" Auditee Response for audit " + auditISM.getCase_number() + " is submitted");
			recentActivity.setUser(user);
			recentActivityMapper.addRecentActivity(recentActivity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while submitting audit in auditee division side: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/clear/ism")
	public ResponseEntity<HttpStatus> clearISMFinding(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {

		try {
			auditeeISMService.clearDuplicateISMFinding(auditISM);
			// User user = functionalitiesService.getUserFromHttpRequest(request);
			// // AuditISM auditISM = auditISMMapper.getAudit(auditISM.getId());
			// recentActivity.setMessage(" Duplicate findings are cleared.");
			// recentActivity.setUser(user);
			// recentActivityMapper.addRecentActivity(recentActivity);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while submitting audit in auditee division side: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/submit/selected/ism")
	public ResponseEntity<HttpStatus> submitSelectedISMFinding(@RequestBody List<AuditISM> auditISMs,
			HttpServletRequest request) {

		try {
			auditeeISMEnhancedService.submitAndEmailSelectedISMFinding(auditISMs, request);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while submitting selected audits in auditee division side: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
