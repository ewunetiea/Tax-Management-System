package com.afr.fms.Common.Report.IS_Report.Entity;

import com.afr.fms.Model.MGT.AssetAndLiability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ISManagementAuditDTO {
    private Long id;
    private String case_number;
    private String directorate_name;
    private String division_name;
    private String target_division_name;
    private String risk_level;
    private String rectification_status;
    private String rectification_status_category;
    private String finding_status;
    private String category;
    private String   audit_finding_detail;
    private String recitified_on;
    private Double amount;
    private String finding_detail;
    private String auditee_response;
    private String fcy;
    private String cash_type;
    private String audit_recommendation;
    private String audit_finding;
    private String audit_impact;
    private String finding_identified_on;
    private String auditee_id;
     private String audit_type;
    private AssetAndLiability assetAndLiability;
    private String reviewed_date;
    private String approved_date;
    private String finding_date;
    private String responded_date; 
}
