package com.afr.fms.Common.Report.Higher_Official;



// import com.afr.fms.Branch_Audit.Report.Model.donTopenthis.ReportBranch_responseHigherOfficials;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Common.Report.Higher_Official.models.ReportBranchRequestHigherOfficials;
import com.afr.fms.Common.Report.Higher_Official.models.it_is_all_boring.ReportHigherOfficials_response_branch;

@Service
public class ReportService_HigherOfficials_branch {

  @Autowired
  ReportMapper_HigherOfficials reportMapper;


  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_atm(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_atm(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_incomplete(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_incomplete(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_operational(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_operational(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_dormant(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_dormant(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  // public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_observation(
  //   ReportBranchRequestHigherOfficials report_request
  // ) {
  //       return reportMapper
  //       .fetchReportHigherOfficials_branch_observation(
  //           report_request.getRegion(),
  //           report_request.getBranch(),
  //           report_request.getModule_type(),
  //           report_request.getRisk_level(),
  //           report_request.getAmount_min(),
  //           report_request.getAmount_max(),
  //           report_request.getUser_id(),
  //           report_request.getUser_roles());
  // }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_abnormal(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_abnormal(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_asset_liability(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_asset_liability(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_controllable(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_controllable(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_suspense(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_suspense(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_memo(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_memo(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_negotiable(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_negotiable(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_long(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_long(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_loan(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_loan(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_cash_performance(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_cash_performance(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_cash_mgt(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_cash_mgt(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

  public List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_cash_count(
    ReportBranchRequestHigherOfficials report_request
  ) {
        return reportMapper
        .fetchReportHigherOfficials_branch_cash_count(
            report_request.getRegion(),
            report_request.getBranch(),
            report_request.getModule_type(),
            report_request.getRisk_level(),
            report_request.getAmount_min(),
            report_request.getAmount_max(),
            report_request.getUser_id(),
            report_request.getUser_roles());
  }

}
