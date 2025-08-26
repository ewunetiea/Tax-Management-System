package com.afr.fms.Model.MGT;

import lombok.Data;

@Data
public class MemorandomAndContingent {

 private Long  id;
 private Long is_mgt_audit_id;
  private Double memorandom_amount;
  private Double contingent_amount;
  private Double difference;
  private String cash_type;
  private String fcy;

}

    
