
package com.afr.fms.Model.MGT;

import java.util.List;

import com.afr.fms.Admin.Entity.Branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoanAndAdvance {
  private Long id;
  private String borrowerName;
  private String loanGrantedDate;
  private String loanSettlementDate;
  private Double loanInterestRate;
  private Double penalty_charge;
  private Double arrearsAmount;
  private String loan_due_date;
  private Double penality_charge_collected;
  private Double penality_charge_uncollected;
  private Double interest_income;
  private Double interest_income_collected;
  private Double interest_income_uncollected;
  private Double fees_and_charges;
  private Double fee_charge_collected;
  private Double fee_charge_uncollected;
  private String collateral_type;
  private Double value_of_collateral;
  private String cash_type;
  private String fcy;
  private Double one_percent_stamp_duty_charge;
  private Double one_percent_stamp_duty_charge_collected;
  private Double one_percent_stamp_duty_charge_uncollected;
  private Long over_draft_id;
  private String loan_status;
  private Branch branch;
  private Long is_mgt_audit_id;

  private String accountNumber;
  private String loanType;
  private Double loanAmount;
  private Double outstanding_balane;
  private Double outstandingBalance;
  private String loanRepaymentAndInstallment;
  private String loanCategory;
  private Double repayment_amount;

}
