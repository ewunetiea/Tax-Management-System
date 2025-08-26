package com.afr.fms.Common.Report.Higher_Official.models;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportBranch_responseHigherOfficials_response_is_mgt {

  public Long id;
  private String finding;
  private String impact;
  private String recommendation;
  private String risk_level;
  private String cash_type;
  private String fcy;
  private String auditee;
  private double amount;
  private String auditee_response;
  private String finding_date;
}
