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
public class ReportBranch_responseHigherOfficials_response_inspection {

  private Long id;

  //cheque_printed_not_delivered_inspection
  private int cpndi_cheque_printed_not_delivered_inspection_total;
  private int cpndi_number_of_days_hold_by_branch;

   //actual_performance_inspection
  private int api_actual_performance_inspection_total;
  private String api_actual_performance_in_amount;
  private String api_actual_performance_in_number;
  private String api_plan_in_amount;
  private String api_plan_in_number;
  private String api_variation_in_amount;
  private String api_variation_in_number;

  //cash_performance_inspection
  private int cpi_cash_performance_inspection_total;
  private String cpi_last_year_shortage_amount;
  private String cpi_last_year_shortage_frequency;
  private String cpi_last_year_excess_amount;
  private String cpi_last_year_excess_frequency;
  private String cpi_recent_year_excess_amount;
  private String cpi_recent_year_excess_frequency;
  private String cpi_recent_year_shortage_amount;
  private String cpi_recent_year_shortage_frequency;
  private String cpi_difference_in_shortage;
  private String cpi_difference_in_excess;
  private String cpi_difference_shortage_frequency;
  private String cpi_difference_in_excess_frequency;

  //cash_holding_limit_inspection
  private int chli_cash_holding_limit_inspection_total;
  private String chli_cash_holding_limit;
  private String chli_average_cash_limit;
  private String chli_difference;

  //cash_count_inspection
  private int cci_cash_count_inspection_total;
  private String cci_actual_cash_count;
  private String cci_trial_balance;
  private String cci_difference;

  //age_of_item_inspection
  private int aoii_age_of_item_inspection_total;
  private int aoii_total_amount;
  private int aoii_total_number;

  //supplies_stock_account_inspection
  private int ssai_supplies_stock_account_inspection_total;
  private String ssai_number_of_checkbook;
  private String ssai_unit_cost;
  private String ssai_trial_balance_amount;
  private String ssai_difference;
  private String ssai_amount;

  //staff_loan_inspection
  private int sli_staff_loan_inspection_total;
  private String sli_staff_loan_amount_as_per_schedule;
  private String sli_staff_loan_amount_as_per_trial_balance;
  private String sli_difference;

  //loan_and_advance_inspection
  private int laai_loan_and_advance_inspection_total;
  private String laai_loan_amount;
  private String laai_outstanding_balance;
  private String laai_arrears_amount;
  private String laai_fees_and_charges;
  private String laai_interest_income;
  private String laai_penalty_charge;

  //accrued_interest_reciviable_inspection
  private int airi_accrued_interest_reciviable_inspection_total;
  private String airi_trial_balance;
  private String airi_accrual_balance;
  private String airi_difference;

  //account_balance_inspection
  private int abi_account_balance_inspection_total;
  private String abi_tracer_balance;
  private String abi_system_balance;
  private String abi_difference;

  //controllable_expense_inspection
  private int cei_controllable_expense_inspection_total;
  private String cei_previous_period_figure;
  private String cei_current_period_figure;
  private String cei_variation;
  private String cei_percentage;

  //deposit_account_inspection
  private int dai_deposit_account_inspection_total;
  private String dai_abnormal_balance;

  //vouching_inspection
  private int vi_vouching_inspection_total;
  private String vi_amount;

  //insufficient_cheque_inspection
  private int ici_insufficient_cheque_inspection_total;
  private String ici_frequency;
  private String ici_cheque_amount;

  //cheque_delivered_not_sign
  private int cdns_cheque_delivered_not_sign_total;
  private int cdns_number_of_days;
}
