package com.afr.fms.Common.Report.MGT_Report.Controller;

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
import com.afr.fms.Common.Report.IS_Report.Entity.CreditDocumentationReport;
import com.afr.fms.Common.Report.IS_Report.Entity.ISManagementAuditDTO;
import com.afr.fms.Common.Report.IS_Report.Entity.ISReport;
import com.afr.fms.Common.Report.MGT_Report.Service.Mgt_Report_Service;
import com.afr.fms.Common.Report.MGT_Report.Service.NewMgt_Report_Service;

@RestController
@RequestMapping("/api/mgt_report")
public class Mgt_Report_Controller {
	@Autowired
	Mgt_Report_Service mgt_Report_Service;

	@Autowired
	private NewMgt_Report_Service newMgt_Report_Service;

	@PostMapping("/search")
	public ResponseEntity<List<ISManagementAuditDTO>> getMgtReport(@RequestBody ISReport isReport,
			HttpServletRequest request) {

		try {
			return new ResponseEntity<>(mgt_Report_Service.getManagementReport(isReport), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/division/{directorate_id}")
	public ResponseEntity<List<Branch>> getMgtReport(@PathVariable Long directorate_id, HttpServletRequest request) {

		try {
			return new ResponseEntity<>(mgt_Report_Service.getDivisionsByDirectorateId(directorate_id), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/fetch_new_report")
	public ResponseEntity<List<ISManagementAuditDTO>> getNewMgtReport(@RequestBody ISReport isReport,
			HttpServletRequest request) {
		try {
			return new ResponseEntity<>(newMgt_Report_Service.getManagementReport(isReport), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/fetch_credit_documentation_report")
	public ResponseEntity<List<ISManagementAuditDTO>> fetchCreditDocumentationNewReport(
			@RequestBody CreditDocumentationReport isReport, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(newMgt_Report_Service.fetchCreditDocumentationNewReport(isReport),
					HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
