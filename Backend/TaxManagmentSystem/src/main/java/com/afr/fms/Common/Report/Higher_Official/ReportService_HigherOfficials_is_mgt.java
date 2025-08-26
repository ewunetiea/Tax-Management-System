package com.afr.fms.Common.Report.Higher_Official;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Common.Report.Higher_Official.models.ReportBranchRequestHigherOfficials;
import com.afr.fms.Common.Report.Higher_Official.models.ReportBranch_responseHigherOfficials_response_is_mgt;

@Service
public class ReportService_HigherOfficials_is_mgt {

  @Autowired
  ReportMapper_HigherOfficials reportMapper;

  public List<ReportBranch_responseHigherOfficials_response_is_mgt> fetchReportHigherOfficials(
    ReportBranchRequestHigherOfficials report_request
  ) {

      return reportMapper.fetchReportHigherOfficials_is_mgt(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );
  }
}
