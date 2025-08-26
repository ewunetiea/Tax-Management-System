package com.afr.fms.Common.Report.Higher_Official.models;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportBranch_responseHigherOfficials {

  private Long id;
  //cheque_printed_not_delivered_inspection
  int cheque_printed_not_delivered_inspection_total;
  int number_of_days_hold_by_branch;
  //actual_performance_inspection
  int actual_performance_inspection_total;
  String actual_performance_in_amount;
  String actual_performance_in_number;
  String plan_in_amount;
  String plan_in_number;
  String variation_in_amount;
  String variation_in_number;
  //cash_performance_inspection
  int cash_performance_inspection_total;
  String last_year_shortage_amount;
  String last_year_shortage_frequency;
  String last_year_excess_amount;
  String last_year_excess_frequency;
  String recent_year_excess_amount;
  String recent_year_excess_frequency;
  String recent_year_shortage_amount;
  String recent_year_shortage_frequency;
  String difference_in_shortage;
  String difference_in_excess;
  String difference_shortage_frequency;
  String difference_in_excess_frequency;
  //cash_holding_limit_inspection
  int cash_holding_limit_inspection_total;
  String cash_holding_limit;
  String average_cash_limit;
  String difference;
  //cash_count_inspection
  int cash_count_inspection_total;
  String actual_cash_count;
  String trial_balance;
  // String difference;
  //age_of_item_inspection
  int age_of_item_inspection_total;
  int total_amount;
  int total_number;
  //supplies_stock_account_inspection
  int supplies_stock_account_inspection_total;
  String number_of_checkbook;
  String unit_cost;
  String trial_balance_amount;
  // String difference;
  String amount;
  //staff_loan_inspection
  int staff_loan_inspection_total;
  String staff_loan_amount_as_per_schedule;
  String staff_loan_amount_as_per_trial_balance;
  // String difference;
  //loan_and_advance_inspection
  int loan_and_advance_inspection_total;
  String loan_amount;
  String outstanding_balance;
  String arrears_amount;
  String fees_and_charges;
  String interest_income;
  String penalty_charge;
  //accrued_interest_reciviable_inspection
  int accrued_interest_reciviable_inspection_total;
  // String trial_balance;
  String accrual_balance;
  // String difference;
  //account_balance_inspection
  int account_balance_inspection_total;
  String tracer_balance;
  String system_balance;
  // String difference;
  //controllable_expense_inspection
  int controllable_expense_inspection_total;
  String previous_period_figure;
  String current_period_figure;
  String variation;
  String percentage;
  //deposit_account_inspection
  int deposit_account_inspection_total;
  String abnormal_balance;
  //vouching_inspection
  int vouching_inspection_total;
  // String amount;
  //insufficient_cheque_inspection
  int insufficient_cheque_inspection_total;
  String frequency;
  String cheque_amount;
  //cheque_delivered_not_sign
  int cheque_delivered_not_sign_total;
  int number_of_days;
}
