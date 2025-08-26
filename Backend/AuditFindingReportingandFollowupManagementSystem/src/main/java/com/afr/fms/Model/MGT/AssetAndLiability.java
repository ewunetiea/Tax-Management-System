package com.afr.fms.Model.MGT;

import lombok.Data;

@Data

public class AssetAndLiability
{
private Long  id;
  private Long is_mgt_audit_id;
  private Double asset_amount;
  private  Double  liability_amount;
  private Double difference;
  private String cash_type;
 private String  fcy;
    
}