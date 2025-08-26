package com.afr.fms.Auditor.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LongOutstandingItems {
    private Long id;
    private Long mgt_audit_id;
    private String justification;
    private String outstanding_item;
    private Double less_than_90_amount;
    private int less_than_90_number;
    private Double greater_than_90_amount;
    private int greater_than_90_number;
    private Double greater_than_180_amount;
    private int greater_than_180_number;
    private Double greater_than_360_amount;
    private int greater_than_360_number;
    private Double trial_balance;
    private Double total_amount;
    private Double difference;
    private String selected_item_age;
}