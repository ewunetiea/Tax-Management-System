package com.afr.fms.Common.FeesChargeCommission.Model;

import com.afr.fms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeChargeCommissionType {
private Long id;
private String type;
private String created_date;
private String modified_date;
private User user;
private Long category_id ;

private String category;
    
}
