package com.afr.fms.Approver.Controller;

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

import com.afr.fms.Approver.Service.AssetLiabilityApproverservice;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;

@RestController
@RequestMapping("/api/approver")
public class AssetLiabilityApproverController {

	@Autowired
	private AssetLiabilityApproverservice assetLiabilityApproverservice;

	RecentActivity recentActivity = new RecentActivity();
	private static final Logger logger = LoggerFactory.getLogger(AssetLiabilityApproverController.class);

	@PostMapping("/asset-liability")
	public ResponseEntity<List<AuditISM>> getFindingBasedOnStatus(@RequestBody PaginatorPayLoad paginatorPayload,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(assetLiabilityApproverservice.getFindingBasedOnStatus(paginatorPayload),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error fetching pending audits for a user ID: {} - {}", paginatorPayload.getUser_id(),
					ex.getMessage(), ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
