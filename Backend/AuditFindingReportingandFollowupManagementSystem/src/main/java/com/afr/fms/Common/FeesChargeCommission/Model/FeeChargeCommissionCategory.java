package com.afr.fms.Common.FeesChargeCommission.Model;

import java.util.List;

import com.afr.fms.Admin.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeChargeCommissionCategory {
private Long id;
private String category;
private String created_date;
private String modified_date;
private User user;
private List<FeeChargeCommissionType> feetype;
    
}
