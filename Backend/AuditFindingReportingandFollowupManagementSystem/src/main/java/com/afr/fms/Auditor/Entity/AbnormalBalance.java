package com.afr.fms.Auditor.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbnormalBalance {
    private Long id;
    private Long is_management_audit_id;
    private Double abnormal_balance;
    private String account_number;
    private String account_type;
    private String abnormal_type;
}