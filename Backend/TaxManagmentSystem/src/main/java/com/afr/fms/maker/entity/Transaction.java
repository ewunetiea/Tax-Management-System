package com.afr.fms.Maker.entity;

import java.sql.Date;

import lombok.Data;

@Data

public class Transaction
{
   private Long id;
    private String transactionType;
    private Double amount;
   private Date transactionDate;
    private Double taxAmount;
}