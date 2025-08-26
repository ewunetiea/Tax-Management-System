package com.afr.fms.Common.Report.Higher_Official.models.it_is_all_boring;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportHigherOfficials_response_branch {

  // // incomplete_account_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private long total;
  // private String account_opened_amount;

  // incomplete_account_branch
  private Long id;
  private String finding;
  private String impact;
  private String recommendation;
  private long total;
  private String account_opened_amount;

  // cash_management_branch
  //   private Long id;
  //   private String finding;
  //   private String impact;
  //   private String recommendation;
  //   private int total;
  private String average_cash_holding;
  private String branch_cash_set_limit;
  private String mid_rate_fcy;
  private String difference;

  // cash_performance_branch
  //   private Long id;
  //   private String finding;
  //   private String impact;
  //   private String recommendation;
  //   private int total;
  private String amount_shortage;
  private String amount_excess;

  // cash_count_branch
  //   private Long id;
  //   private String finding;
  //   private String impact;
  //   private String recommendation;
  //   private int total;
  private String actual_cash_count;
  private String trial_balance;
  private String atm_amount_branch;
  private String atm_amount_lobby;
  //   private String difference;

  // controllable_expense_branch
  //   private Long id;
  //   private String finding;
  //   private String impact;
  //   private String recommendation;
  //   private int total;
  private String variation;
  private String budget_per_plan;
  private String actual_balance;

  // suspense_account_branch
  //   private Long id;
  //   private String finding;
  //   private String impact;
  //   private String recommendation;
  //   private int total;
  //   private String difference;
  private String balance_per_tracer;
  private String balance_per_trial_balance;

  // memorandom_contingent_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  private String memorandom_amount;
  // private String difference;
  private String contingent_amount;

  // loan_advance_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  private String amount_granted;
  private String arrears;

  // long_outstanding_item_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  private long total_number;
  // private String difference;
  // private String trial_balance;
  private String total_amount;

  // negotiable_instrument_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  private long quantity;
  // private String difference;
  private String unit_price;
  private String amount;
  // private String trial_balance;

  // operational_descripancy_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  // private String amount;

  // dormant_inactive_account_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  // private String amount;

  // ATM_card_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  private long issued_card;
  private long distributed_card;
  private long returned_card;
  private long remaining_card;

  // asset_liability_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  private String asset_amount;
  private String liability_amount;
  // private String difference;

  // abnormal_balance_branch
  // private Long id;
  // private String finding;
  // private String impact;
  // private String recommendation;
  // private int total;
  private String debit;
  // private String difference;
  private String credit;
}
