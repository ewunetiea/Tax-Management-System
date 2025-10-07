package com.afr.fms.Maker.entity;

import java.sql.Date;

import lombok.Data;

@Data

public class TaxRule
{
private Long  id;
    private String taxName;
  private Double   taxRate;
  private String   taxType;
   private Date effectiveDate;
   private String  status;
    
}