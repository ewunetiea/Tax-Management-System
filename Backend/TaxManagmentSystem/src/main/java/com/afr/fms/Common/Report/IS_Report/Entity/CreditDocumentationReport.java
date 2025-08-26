package com.afr.fms.Common.Report.IS_Report.Entity;

import java.util.List;

import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.Region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDocumentationReport {
    private String role;
    private Long userId;
    private Long directorateId;
    private Long branchId;
    private String riskLevel;
    private List<String> auditPeriod;
    private String startAuditPeriod;
    private String endAuditPeriod;

    private Integer rectificationStatus;
    private String findingStatus;
    private String category;
    private List<String> rectificationDate;
    private List<String> draftedDate;
    private List<String> reviewedDate;
    private List<String> approvedDate;
    private List<String> respondedDate;
    private String startRectificationDate;
    private String endRectificationDate;
    private String startDraftedDate;
    private String endDraftedDate;
    private String startReviewedDate;
    private String endReviewedDate;
    private String startApprovedDate;
    private String endApprovedDate;
    private String startRespondedDate;
    private String endRespondedDate;

    
    private List<Double> amount;
    private Double min_amount;
    private Double max_amount;
    private String singleSelection;
    private String finding;
    private String audit_type;



    
 private Region region;
  private Branch branch;
  private String finding_status;
 private  List<String>rectification_date_range;
  private List<String> date_range;
  private String audit_finding;
  private String rectification_status;
  private String  single_filter_info;
 private Long user_id;
  private List<String> user_roles;
 private Long user_region_id;

 private String  banking;
private String  collateral_type;
 private    List<String>  loan_granted_date;
 private    List<String> loan_settlement_date;
  private Double loan_amount_min;
  private Double loan_amount_max;
 private String  loan_type;
 private String account_number;
private String cash_type;
private Double penality_charge_collected;
private Double penality_charge_uncollected;
private String penality_charge_collection_status;
private Double interest_income_collected;
private Double interest_income_uncollected;
private String interest_income_collection_status;
private Double fee_charge_collected;
private Double fee_charge_uncollected;
private Double stamp_duty_charge_min;
private Double stamp_duty_charge_max;
private Double interest_income_min;
private Double interest_income_max;
private Double penality_charge_min;
private Double penality_charge_max;
private Double fees_and_charge_min;
private Double fees_and_charge_max;
private List<String> reviewed_date_ranges;
private List<String> approved_date_ranges;
private List<String> responded_date_ranges;
private Boolean progress_report;
private String borrowerName;
private String responded_date;

private List<String> loanGrantedDate;
private List<String> loanSetlmentDate;
private String case_number;

}
