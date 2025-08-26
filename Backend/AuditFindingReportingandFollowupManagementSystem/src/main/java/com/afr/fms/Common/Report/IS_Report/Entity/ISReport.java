package com.afr.fms.Common.Report.IS_Report.Entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ISReport {
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
    private String startRectificationDate;
    private String endRectificationDate;
    private List<Double> amount;
    private Double min_amount;
    private Double max_amount;
    private String singleSelection;
    private String finding;
    private String audit_type;
    private List<String> draftedDate;
    private List<String> reviewedDate;
    private List<String> approvedDate;
    private List<String> respondedDate;
    private String case_number;
}
