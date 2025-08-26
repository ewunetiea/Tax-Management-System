package com.afr.fms.Common.Report.Higher_Official;


import com.afr.fms.Common.Report.Higher_Official.models.ReportBranchRequestHigherOfficials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/branch/report/")
public class ReportController_HigherOfficials {

  @Autowired
  ReportService_HigherOfficials_is_mgt reportService_is_mgt;

  @Autowired
  ReportService_HigherOfficials_inspection reportService_inspection;

  @Autowired
  ReportService_HigherOfficials_branch reportService_report;

  @PostMapping("fetchReportHigherOfficials")
  public ResponseEntity<?> fetchReportHigherOfficials(
    @RequestBody ReportBranchRequestHigherOfficials report_request
  ) {
    try {
      if (
        report_request.getModule_type().equalsIgnoreCase("is") ||
        report_request.getModule_type().equalsIgnoreCase("Management")
      ) {
        return ResponseEntity
          .status(HttpStatus.OK)
          .body(
            reportService_is_mgt.fetchReportHigherOfficials(report_request)
          );
      } else if (
        report_request.getModule_type().equalsIgnoreCase("Inspection")
      ) {
        return ResponseEntity
          .status(HttpStatus.OK)
          .body(
            reportService_inspection.fetchReportHigherOfficials(report_request)
          );
      } else if (
        report_request.getModule_type().equalsIgnoreCase("Branch Financial")
      ) {
        /*
      { label: "Cash Count", value: "cash_count" },

          */
        if (report_request.getAudit_type().equalsIgnoreCase("atm")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_atm(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("incomplete")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_incomplete(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("operational")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_operational(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("dormant")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_dormant(
                report_request
              )
            );
        // } else if (report_request.getAudit_type().equalsIgnoreCase("observation")) {
        //   return ResponseEntity
        //     .status(HttpStatus.OK)
        //     .body(
        //       reportService_report.fetchReportHigherOfficials_branch_observation(
        //         report_request
        //       )
        //     );
        } else if (report_request.getAudit_type().equalsIgnoreCase("abnormal")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_abnormal(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("asset_liability")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_asset_liability(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("controllable")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_controllable(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("suspense")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_suspense(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("memo")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_memo(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("negotiable")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_negotiable(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("long")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_long(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("loan")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_loan(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("cash_performance")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_cash_performance(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("cash_mgt")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_cash_mgt(
                report_request
              )
            );
        } else if (report_request.getAudit_type().equalsIgnoreCase("cash_count")) {
          return ResponseEntity
            .status(HttpStatus.OK)
            .body(
              reportService_report.fetchReportHigherOfficials_branch_cash_count(
                report_request
              )
            );
        }
        return null;
      } else {}
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
