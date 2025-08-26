package com.afr.fms.Common.FraudCase;

import com.afr.fms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraudCase {
private Long id;
private String format;
private Long initial;
private String created_date;
private String updated_date;
private String case_type;
private User user;
private Boolean status;
private User approver;
private int fraud_status;

    
}
