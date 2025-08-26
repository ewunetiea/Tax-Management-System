package com.afr.fms.Common.Report.IS_Report.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.afr.fms.Common.Report.IS_Report.Entity.ISManagementAuditDTO;
import com.afr.fms.Common.Report.IS_Report.Entity.ISReport;
import com.afr.fms.Common.Report.IS_Report.Service.ISMGTService;
import com.afr.fms.Common.Report.IS_Report.Service.ISReportService;

@RestController
@RequestMapping("/api/is_report")

public class ISReportController {

	@Autowired
	ISReportService is_Report_Service;


	@Autowired
	ISMGTService ismgtService;

	@PostMapping("/search")
	public ResponseEntity<List<ISManagementAuditDTO>> getISReport(@RequestBody ISReport isReport,
			HttpServletRequest request) {
			try {
				return new ResponseEntity<>(ismgtService.getISReport(isReport),

						HttpStatus.OK);
			} catch (Exception ex) {
				System.out.println(ex);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		

	}

	@GetMapping("/division/{directorate_id}")
	public ResponseEntity<List<Branch>> getDivisionsByDirectorateIdForIS(@PathVariable Long directorate_id,
			HttpServletRequest request) {
			try {
				return new ResponseEntity<>(is_Report_Service.getDivisionsByDirectorateId(directorate_id),
						HttpStatus.OK);
			} catch (Exception ex) {
				System.out.println(ex);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		

	}

}
