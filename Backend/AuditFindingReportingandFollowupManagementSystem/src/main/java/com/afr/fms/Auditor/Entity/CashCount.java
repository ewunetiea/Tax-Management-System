package com.afr.fms.Auditor.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashCount implements Serializable {
    private Long id;
    private Long is_mgt_audit_id;
    private String accountable_staff;
    private Double actual_cash_count;
    private Double trial_balance;
    private Double difference;
}