package com.afr.fms.Model.MGT;

import lombok.Data;

@Data
public class SuspenseAccount {
    private Long id;
    private Long is_mgt_audit_id;
    private Double balance_per_tracer;
    private Double balance_per_trial_balance;
    private Double difference;
    private String tracer_date;
    private String cash_type;
    private String fcy;

    private String account_number;

    private String account_type;

}
