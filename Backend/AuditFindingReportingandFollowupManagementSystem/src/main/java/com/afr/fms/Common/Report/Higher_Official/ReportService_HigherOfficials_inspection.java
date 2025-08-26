package com.afr.fms.Common.Report.Higher_Official;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Common.Report.Higher_Official.models.ReportBranchRequestHigherOfficials;
import com.afr.fms.Common.Report.Higher_Official.models.ReportBranch_responseHigherOfficials;
import com.afr.fms.Common.Report.Higher_Official.models.ReportBranch_responseHigherOfficials_response_inspection;

@Service
public class ReportService_HigherOfficials_inspection {

  @Autowired
  ReportMapper_HigherOfficials reportMapper;

  public ReportBranch_responseHigherOfficials_response_inspection fetchReportHigherOfficials(
    ReportBranchRequestHigherOfficials report_request
  ) {

    ReportBranch_responseHigherOfficials_response_inspection response = new ReportBranch_responseHigherOfficials_response_inspection();

      //cheque_printed_not_delivered_inspection
      ReportBranch_responseHigherOfficials cheque_printed_not_delivered_inspection = reportMapper.fetchReportHigherOfficials_cheque_printed_not_delivered_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setCpndi_cheque_printed_not_delivered_inspection_total(
        cheque_printed_not_delivered_inspection.getCheque_printed_not_delivered_inspection_total()
      );
      response.setCpndi_number_of_days_hold_by_branch(
        cheque_printed_not_delivered_inspection.getNumber_of_days_hold_by_branch()
      );

      //actual_performance_inspection
      ReportBranch_responseHigherOfficials actual_performance_inspection = reportMapper.fetchReportHigherOfficials_actual_performance_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setApi_actual_performance_in_amount(
        actual_performance_inspection.getActual_performance_in_amount()
      );
      response.setApi_actual_performance_in_number(
        actual_performance_inspection.getActual_performance_in_number()
      );
      response.setApi_actual_performance_inspection_total(
        actual_performance_inspection.getActual_performance_inspection_total()
      );
      response.setApi_plan_in_amount(
        actual_performance_inspection.getPlan_in_amount()
      );
      response.setApi_plan_in_number(
        actual_performance_inspection.getPlan_in_number()
      );
      response.setApi_variation_in_amount(
        actual_performance_inspection.getVariation_in_amount()
      );
      response.setApi_variation_in_number(
        actual_performance_inspection.getVariation_in_number()
      );

      //cash_performance_inspection
      ReportBranch_responseHigherOfficials cash_performance_inspection = reportMapper.fetchReportHigherOfficials_cash_performance_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setCpi_cash_performance_inspection_total(
        cash_performance_inspection.getCash_performance_inspection_total()
      );
      response.setCpi_difference_in_excess(
        cash_performance_inspection.getDifference_in_excess()
      );
      response.setCpi_difference_in_excess_frequency(
        cash_performance_inspection.getDifference_in_excess_frequency()
      );
      response.setCpi_difference_in_shortage(
        cash_performance_inspection.getDifference_in_shortage()
      );
      response.setCpi_difference_shortage_frequency(
        cash_performance_inspection.getDifference_shortage_frequency()
      );
      response.setCpi_last_year_excess_amount(
        cash_performance_inspection.getLast_year_excess_amount()
      );
      response.setCpi_last_year_excess_frequency(
        cash_performance_inspection.getLast_year_excess_frequency()
      );
      response.setCpi_last_year_shortage_amount(
        cash_performance_inspection.getLast_year_shortage_amount()
      );
      response.setCpi_last_year_shortage_frequency(
        cash_performance_inspection.getLast_year_shortage_frequency()
      );
      response.setCpi_recent_year_excess_amount(
        cash_performance_inspection.getRecent_year_excess_amount()
      );
      response.setCpi_recent_year_excess_frequency(
        cash_performance_inspection.getRecent_year_excess_frequency()
      );
      response.setCpi_recent_year_shortage_amount(
        cash_performance_inspection.getRecent_year_shortage_amount()
      );
      response.setCpi_recent_year_shortage_frequency(
        cash_performance_inspection.getRecent_year_shortage_frequency()
      );

      //cash_holding_limit_inspection
      ReportBranch_responseHigherOfficials cash_holding_limit_inspection = reportMapper.fetchReportHigherOfficials_cash_holding_limit_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setChli_cash_holding_limit_inspection_total(
        cash_holding_limit_inspection.getCash_holding_limit_inspection_total()
      );
      response.setChli_average_cash_limit(
        cash_holding_limit_inspection.getAverage_cash_limit()
      );
      response.setChli_cash_holding_limit(
        cash_holding_limit_inspection.getCash_holding_limit()
      );
      response.setChli_difference(
        cash_holding_limit_inspection.getDifference()
      );

      //cash_count_inspection
      ReportBranch_responseHigherOfficials cash_count_inspection = reportMapper.fetchReportHigherOfficials_cash_count_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setCci_cash_count_inspection_total(
        cash_count_inspection.getCash_count_inspection_total()
      );
      response.setCci_actual_cash_count(
        cash_count_inspection.getActual_cash_count()
      );
      response.setCci_difference(cash_count_inspection.getDifference());
      response.setCci_trial_balance(cash_count_inspection.getTrial_balance());

      //age_of_item_inspection
      ReportBranch_responseHigherOfficials age_of_item_inspection = reportMapper.fetchReportHigherOfficials_age_of_item_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setAoii_age_of_item_inspection_total(
        age_of_item_inspection.getAge_of_item_inspection_total()
      );
      response.setAoii_total_amount(age_of_item_inspection.getTotal_amount());
      response.setAoii_total_number(age_of_item_inspection.getTotal_number());

      //supplies_stock_account_inspection
      ReportBranch_responseHigherOfficials supplies_stock_account_inspection = reportMapper.fetchReportHigherOfficials_supplies_stock_account_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setSsai_supplies_stock_account_inspection_total(
        supplies_stock_account_inspection.getSupplies_stock_account_inspection_total()
      );
      response.setSsai_amount(supplies_stock_account_inspection.getAmount());
      response.setSsai_difference(
        supplies_stock_account_inspection.getDifference()
      );
      response.setSsai_number_of_checkbook(
        supplies_stock_account_inspection.getNumber_of_checkbook()
      );
      response.setSsai_trial_balance_amount(
        supplies_stock_account_inspection.getTrial_balance_amount()
      );
      response.setSsai_unit_cost(
        supplies_stock_account_inspection.getUnit_cost()
      );

      //staff_loan_inspection
      ReportBranch_responseHigherOfficials staff_loan_inspection = reportMapper.fetchReportHigherOfficials_staff_loan_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setSli_staff_loan_inspection_total(
        staff_loan_inspection.getStaff_loan_inspection_total()
      );
      response.setSli_difference(staff_loan_inspection.getDifference());
      response.setSli_staff_loan_amount_as_per_schedule(
        staff_loan_inspection.getStaff_loan_amount_as_per_schedule()
      );
      response.setSli_staff_loan_amount_as_per_trial_balance(
        staff_loan_inspection.getStaff_loan_amount_as_per_trial_balance()
      );

      //loan_and_advance_inspection
      ReportBranch_responseHigherOfficials loan_and_advance_inspection = reportMapper.fetchReportHigherOfficials_loan_and_advance_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setLaai_loan_and_advance_inspection_total(
        loan_and_advance_inspection.getLoan_and_advance_inspection_total()
      );
      response.setLaai_arrears_amount(
        loan_and_advance_inspection.getArrears_amount()
      );
      response.setLaai_fees_and_charges(
        loan_and_advance_inspection.getFees_and_charges()
      );
      response.setLaai_interest_income(
        loan_and_advance_inspection.getInterest_income()
      );
      response.setLaai_loan_amount(
        loan_and_advance_inspection.getLoan_amount()
      );
      response.setLaai_outstanding_balance(
        loan_and_advance_inspection.getOutstanding_balance()
      );
      response.setLaai_penalty_charge(
        loan_and_advance_inspection.getPenalty_charge()
      );

      //accrued_interest_reciviable_inspection
      ReportBranch_responseHigherOfficials accrued_interest_reciviable_inspection = reportMapper.fetchReportHigherOfficials_accrued_interest_reciviable_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setAiri_accrued_interest_reciviable_inspection_total(
        accrued_interest_reciviable_inspection.getAccrued_interest_reciviable_inspection_total()
      );
      response.setAiri_accrual_balance(
        accrued_interest_reciviable_inspection.getAccrual_balance()
      );
      response.setAiri_difference(
        accrued_interest_reciviable_inspection.getDifference()
      );
      response.setAiri_trial_balance(
        accrued_interest_reciviable_inspection.getTrial_balance()
      );

      //account_balance_inspection
      ReportBranch_responseHigherOfficials account_balance_inspection = reportMapper.fetchReportHigherOfficials_account_balance_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setAbi_account_balance_inspection_total(
        account_balance_inspection.getAccount_balance_inspection_total()
      );
      response.setAbi_difference(account_balance_inspection.getDifference());
      response.setAbi_system_balance(
        account_balance_inspection.getSystem_balance()
      );
      response.setAbi_tracer_balance(
        account_balance_inspection.getTracer_balance()
      );

      //controllable_expense_inspection
      ReportBranch_responseHigherOfficials controllable_expense_inspection = reportMapper.fetchReportHigherOfficials_controllable_expense_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setCei_controllable_expense_inspection_total(
        controllable_expense_inspection.getControllable_expense_inspection_total()
      );
      response.setCei_current_period_figure(
        controllable_expense_inspection.getCurrent_period_figure()
      );
      response.setCei_percentage(
        controllable_expense_inspection.getPercentage()
      );
      response.setCei_previous_period_figure(
        controllable_expense_inspection.getPrevious_period_figure()
      );
      response.setCei_variation(controllable_expense_inspection.getVariation());

      //deposit_account_inspection
      ReportBranch_responseHigherOfficials deposit_account_inspection = reportMapper.fetchReportHigherOfficials_deposit_account_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setDai_deposit_account_inspection_total(
        deposit_account_inspection.getDeposit_account_inspection_total()
      );
      response.setDai_abnormal_balance(
        deposit_account_inspection.getAbnormal_balance()
      );

      //vouching_inspection
      ReportBranch_responseHigherOfficials vouching_inspection = reportMapper.fetchReportHigherOfficials_vouching_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setVi_vouching_inspection_total(
        vouching_inspection.getVouching_inspection_total()
      );
      response.setVi_amount(vouching_inspection.getAmount());

      //insufficient_cheque_inspection
      ReportBranch_responseHigherOfficials insufficient_cheque_inspection = reportMapper.fetchReportHigherOfficials_insufficient_cheque_inspection(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setIci_insufficient_cheque_inspection_total(
        insufficient_cheque_inspection.getInsufficient_cheque_inspection_total()
      );
      response.setIci_frequency(insufficient_cheque_inspection.getFrequency());
      response.setIci_cheque_amount(
        insufficient_cheque_inspection.getCheque_amount()
      );

      //cheque_delivered_not_sign
      ReportBranch_responseHigherOfficials cheque_delivered_not_sign = reportMapper.fetchReportHigherOfficials_cheque_delivered_not_sign(
        report_request.getRegion(),
        report_request.getBranch(),
        report_request.getModule_type(),
        report_request.getRisk_level(),
        report_request.getAmount_min(),
        report_request.getAmount_max(),
        report_request.getUser_id(),
        report_request.getUser_roles()
      );

      response.setCdns_cheque_delivered_not_sign_total(
        cheque_delivered_not_sign.getCheque_delivered_not_sign_total()
      );
      response.setCdns_number_of_days(
        cheque_delivered_not_sign.getNumber_of_days()
      );


      // System.out.println("id: " + response.id);

      // System.out.println("cpndi_cheque_printed_not_delivered_inspection_total: " + response.cpndi_cheque_printed_not_delivered_inspection_total);
      // System.out.println("cpndi_number_of_days_hold_by_branch: " + response.cpndi_number_of_days_hold_by_branch);

      // System.out.println("api_actual_performance_inspection_total: " + response.api_actual_performance_inspection_total);
      // System.out.println("api_actual_performance_in_amount: " + response.api_actual_performance_in_amount);
      // System.out.println("api_actual_performance_in_number: " + response.api_actual_performance_in_number);
      // System.out.println("api_plan_in_amount: " + response.api_plan_in_amount);
      // System.out.println("api_plan_in_number: " + response.api_plan_in_number);
      // System.out.println("api_variation_in_amount: " + response.api_variation_in_amount);
      // System.out.println("api_variation_in_number: " + response.api_variation_in_number);

      // System.out.println("cpi_cash_performance_inspection_total: " + response.cpi_cash_performance_inspection_total);
      // System.out.println("cpi_last_year_shortage_amount: " + response.cpi_last_year_shortage_amount);
      // System.out.println("cpi_last_year_shortage_frequency: " + response.cpi_last_year_shortage_frequency);
      // System.out.println("cpi_last_year_excess_amount: " + response.cpi_last_year_excess_amount);
      // System.out.println("cpi_last_year_excess_frequency: " + response.cpi_last_year_excess_frequency);
      // System.out.println("cpi_recent_year_excess_amount: " + response.cpi_recent_year_excess_amount);
      // System.out.println("cpi_recent_year_excess_frequency: " + response.cpi_recent_year_excess_frequency);
      // System.out.println("cpi_recent_year_shortage_amount: " + response.cpi_recent_year_shortage_amount);
      // System.out.println("cpi_recent_year_shortage_frequency: " + response.cpi_recent_year_shortage_frequency);
      // System.out.println("cpi_difference_in_shortage: " + response.cpi_difference_in_shortage);
      // System.out.println("cpi_difference_in_excess: " + response.cpi_difference_in_excess);
      // System.out.println("cpi_difference_shortage_frequency: " + response.cpi_difference_shortage_frequency);
      // System.out.println("cpi_difference_in_excess_frequency: " + response.cpi_difference_in_excess_frequency);

      // System.out.println("chli_cash_holding_limit_inspection_total: " + response.chli_cash_holding_limit_inspection_total);
      // System.out.println("chli_cash_holding_limit: " + response.chli_cash_holding_limit);
      // System.out.println("chli_average_cash_limit: " + response.chli_average_cash_limit);
      // System.out.println("chli_difference: " + response.chli_difference);

      // System.out.println("cci_cash_count_inspection_total: " + response.cci_cash_count_inspection_total);
      // System.out.println("cci_actual_cash_count: " + response.cci_actual_cash_count);
      // System.out.println("cci_trial_balance: " + response.cci_trial_balance);
      // System.out.println("cci_difference: " + response.cci_difference);

      // System.out.println("aoii_age_of_item_inspection_total: " + response.aoii_age_of_item_inspection_total);
      // System.out.println("aoii_total_amount: " + response.aoii_total_amount);
      // System.out.println("aoii_total_number: " + response.aoii_total_number);

      // System.out.println("ssai_supplies_stock_account_inspection_total: " + response.ssai_supplies_stock_account_inspection_total);
      // System.out.println("ssai_number_of_checkbook: " + response.ssai_number_of_checkbook);
      // System.out.println("ssai_unit_cost: " + response.ssai_unit_cost);
      // System.out.println("ssai_trial_balance_amount: " + response.ssai_trial_balance_amount);
      // System.out.println("ssai_difference: " + response.ssai_difference);
      // System.out.println("ssai_amount: " + response.ssai_amount);

      // System.out.println("sli_staff_loan_inspection_total: " + response.sli_staff_loan_inspection_total);
      // System.out.println("sli_staff_loan_amount_as_per_schedule: " + response.sli_staff_loan_amount_as_per_schedule);
      // System.out.println("sli_staff_loan_amount_as_per_trial_balance: " + response.sli_staff_loan_amount_as_per_trial_balance);
      // System.out.println("sli_difference: " + response.sli_difference);

      // System.out.println("laai_loan_and_advance_inspection_total: " + response.laai_loan_and_advance_inspection_total);
      // System.out.println("laai_loan_amount: " + response.laai_loan_amount);
      // System.out.println("laai_outstanding_balance: " + response.laai_outstanding_balance);
      // System.out.println("laai_arrears_amount: " + response.laai_arrears_amount);
      // System.out.println("laai_fees_and_charges: " + response.laai_fees_and_charges);
      // System.out.println("laai_interest_income: " + response.laai_interest_income);
      // System.out.println("laai_penalty_charge: " + response.laai_penalty_charge);

      // System.out.println("airi_accrued_interest_reciviable_inspection_total: " + response.airi_accrued_interest_reciviable_inspection_total);
      // System.out.println("airi_trial_balance: " + response.airi_trial_balance);
      // System.out.println("airi_accrual_balance: " + response.airi_accrual_balance);
      // System.out.println("airi_difference: " + response.airi_difference);

      // System.out.println("abi_account_balance_inspection_total: " + response.abi_account_balance_inspection_total);
      // System.out.println("abi_tracer_balance: " + response.abi_tracer_balance);
      // System.out.println("abi_system_balance: " + response.abi_system_balance);
      // System.out.println("abi_difference: " + response.abi_difference);

      // System.out.println("cei_controllable_expense_inspection_total: " + response.cei_controllable_expense_inspection_total);
      // System.out.println("cei_previous_period_figure: " + response.cei_previous_period_figure);
      // System.out.println("cei_current_period_figure: " + response.cei_current_period_figure);
      // System.out.println("cei_variation: " + response.cei_variation);
      // System.out.println("cei_percentage: " + response.cei_percentage);

      // System.out.println("dai_deposit_account_inspection_total: " + response.dai_deposit_account_inspection_total);
      // System.out.println("dai_abnormal_balance: " + response.dai_abnormal_balance);

      // System.out.println("vi_vouching_inspection_total: " + response.vi_vouching_inspection_total);
      // System.out.println("vi_amount: " + response.vi_amount);

      // System.out.println("ici_insufficient_cheque_inspection_total: " + response.ici_insufficient_cheque_inspection_total);
      // System.out.println("ici_frequency: " + response.ici_frequency);
      // System.out.println("ici_cheque_amount: " + response.ici_cheque_amount);

      // System.out.println("cdns_cheque_delivered_not_sign_total: " + response.cdns_cheque_delivered_not_sign_total);
      // System.out.println("cdns_number_of_days: " + response.cdns_number_of_days);

      return response;
  }
}
