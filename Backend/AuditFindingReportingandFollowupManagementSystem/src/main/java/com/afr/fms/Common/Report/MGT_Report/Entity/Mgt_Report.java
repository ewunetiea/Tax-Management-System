package com.afr.fms.Common.Report.MGT_Report.Entity;

import java.util.List;

import com.afr.fms.Admin.Entity.Branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mgt_Report {
    private Branch division;
    private Branch directorate;
    private String risk_level;
    private List<String> audit_period;
    private Boolean rectification_status;
    private String finding_status;
    private String finding;
    private String category;
    private List<String> rectification_date;
    private List<Double> amount;
}
