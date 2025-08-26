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
import com.afr.fms.Auditor.Service.AssetAndLiabilityAuditorService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;

@RestController
@RequestMapping("/api/asset-liability")
public class AssetAndLiabilityAuditorController {

	@Autowired
	private AssetAndLiabilityAuditorService assetAndLiabilityAuditorService;

	RecentActivity recentActivity = new RecentActivity();

	private static final Logger logger = LoggerFactory.getLogger(AssetAndLiabilityAuditorController.class);

	@PostMapping("/auditor")
	public ResponseEntity<List<AuditISM>> getFindingBasedOnStatus(@RequestBody PaginatorPayLoad paginatorPayload,
			HttpServletRequest request) {

		try {
			List<AuditISM> findings = assetAndLiabilityAuditorService.getFindingBasedOnStatus(paginatorPayload);

			return new ResponseEntity<>(findings, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("getFindingBasedOnStatus", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/create-edit")
	public ResponseEntity<HttpStatus> createAssetandLiability(@RequestBody AuditISM audit, HttpServletRequest request) {

		try {

			if (audit.getId() != null) {
				assetAndLiabilityAuditorService.UpdateAssetAndLibility(audit);
			} else {
				assetAndLiabilityAuditorService.createAssetAndLiability(audit);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {

			logger.error("createAssetandLiability", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
