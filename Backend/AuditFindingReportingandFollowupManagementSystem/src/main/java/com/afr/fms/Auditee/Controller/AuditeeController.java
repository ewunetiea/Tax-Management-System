package com.afr.fms.Auditee.Controller;

import java.util.List;
import java.util.stream.Collectors;
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
import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditee.Service.AuditeeService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.Permission.Service.FunctionalitiesService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@RestController
@RequestMapping("/api/auditee")
public class AuditeeController {

	@Autowired
	private AuditeeService auditeeISMService;

	@Autowired
	private AuditISMMapper auditISMMapper;

	@Autowired
	private FunctionalitiesService functionalitiesService;

	@Autowired
	private RecentActivityMapper recentActivityMapper;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(AuditeeController.class);

	@PostMapping("/ism")
	public ResponseEntity<HttpStatus> add_auditee_division_ism(@RequestBody IS_MGT_Auditee is_MGTAuditee,
			HttpServletRequest request) {
		try {
			User user = functionalitiesService.getUserFromHttpRequest(request);
			is_MGTAuditee.setAssigner(user);
			auditeeISMService.add_auditee_division_ism(is_MGTAuditee);
			String divisions = "";
			int division_size = is_MGTAuditee.getDivisions().size();
			if (division_size == 1) {
				divisions = is_MGTAuditee.getDivisions().get(0).getName();
			} else {
				int index = 0;
				for (Branch division : is_MGTAuditee.getDivisions()) {
					if (index == (division_size - 1)) {
						divisions = divisions + division.getName();
					} else {
						divisions = divisions + division.getName() + ", ";
					}
					index++;
				}
			}
			String case_number = "";
			if (is_MGTAuditee.getAuditISM_id() != null) {
				AuditISM auditISM = auditISMMapper.getAudit(is_MGTAuditee.getAuditISM_id());
				case_number = auditISM.getCase_number();
			} else {
				case_number = is_MGTAuditee.getAudits().stream()
						.map(AuditISM::getCase_number)
						.collect(Collectors.joining(", "));
			}
			recentActivity.setMessage(divisions + " for audit " + case_number + " is assigned ");
			recentActivity.setUser(user);
			recentActivityMapper.addRecentActivity(recentActivity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while adding auditee divisions: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/pending")
	public ResponseEntity<List<AuditISM>> getAuditsForAuditee(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getAuditsForAuditee(auditISM), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching findings in auditee side: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/partially_rectified/ism/{auditee_id}")
	public ResponseEntity<List<AuditISM>> getPartiallyRectifiedAuditsForAuditee(@PathVariable Long auditee_id,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(auditeeISMService.getPartiallyRectifiedAuditsForAuditee(auditee_id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching get_partially_rectified_audits_for_auditee_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/progress/ism/{auditee_id}")
	public ResponseEntity<List<AuditISM>> getAuditsForAuditeeOnProgress(@PathVariable Long auditee_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditeeISMService.getAuditsForAuditeeOnProgress(auditee_id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching get_audits_for_auditee_on_progress_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/unrectified/ism/{auditee_id}")
	public ResponseEntity<List<AuditISM>> getUnrectifiedAuditsForAuditee(@PathVariable Long auditee_id,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditeeISMService.getUnrectifiedAuditsForAuditee(auditee_id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching get_unrectified_audits_for_auditee_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/responded/ism")
	public ResponseEntity<List<AuditISM>> getRespondedAuditsForAuditee(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(auditeeISMService.getRespondedAuditsForAuditee(auditISM), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while fetching get_responded_audits_for_auditee_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/response/ism")
	public ResponseEntity<HttpStatus> finishAuditeeResponse(@RequestBody AuditISM auditISM,
			HttpServletRequest request) {

		try {

			auditeeISMService.finishAuditeeResponse(auditISM);
			User user = functionalitiesService.getUserFromHttpRequest(request);
			// AuditISM auditISM = auditeeDashboardMapper.getAuditByAuditeeID(auditee_id);
			recentActivity.setMessage(" Auditee Response for audit " + auditISM.getCase_number() + " is submitted");
			recentActivity.setUser(user);
			recentActivityMapper.addRecentActivity(recentActivity);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while finalizing finish_auditee_response_is_mgt: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/reject/ism")
	public ResponseEntity<HttpStatus> rejectFindings(@RequestBody Remark remark,
			HttpServletRequest request) {
		// if (functionalitiesService.verifyPermission(request,
		// "finish_auditee_response_is_mgt")) {
		try {

			auditeeISMService.rejectFindings(remark);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while auditee rejecting findings: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// } else {
		// return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		// }
	}

	@PostMapping("/selected/response/ism/")
	public ResponseEntity<HttpStatus> submitSelectedAuditeeResponse(@RequestBody List<AuditISM> audits,
			HttpServletRequest request) {
		try {
			auditeeISMService.submitSelectedAuditeeResponse(audits);
			User user = functionalitiesService.getUserFromHttpRequest(request);
			for (AuditISM auditISM : audits) {
				recentActivity
						.setMessage(" Auditee Response for audit " + auditISM.getCase_number() + " is submitted");
				recentActivity.setUser(user);
				recentActivityMapper.addRecentActivity(recentActivity);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error occurred while finalizing selected findings in finish_auditee_response_is_mgt: ",
					ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
