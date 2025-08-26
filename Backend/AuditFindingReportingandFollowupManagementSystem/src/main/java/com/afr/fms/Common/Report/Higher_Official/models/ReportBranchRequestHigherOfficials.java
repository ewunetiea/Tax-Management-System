package com.afr.fms.Common.Report.Higher_Official.models;


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
public class ReportBranchRequestHigherOfficials {
	private Long region;
	private Long branch;
	private String module_type;
	private String audit_type;
	private String risk_level;
	private Double amount_min;
	private Double amount_max;
	private Long user_id;
	private String[] user_roles;
}
