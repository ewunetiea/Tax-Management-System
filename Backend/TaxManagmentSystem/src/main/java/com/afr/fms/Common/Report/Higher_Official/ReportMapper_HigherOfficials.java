package com.afr.fms.Common.Report.Higher_Official;


// import com.afr.fms.Branch_Audit.Report.Model.donTopenthis.ReportBranch_responseHigherOfficials;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.afr.fms.Common.Report.Higher_Official.models.ReportBranch_responseHigherOfficials;
import com.afr.fms.Common.Report.Higher_Official.models.ReportBranch_responseHigherOfficials_response_is_mgt;
import com.afr.fms.Common.Report.Higher_Official.models.it_is_all_boring.ReportHigherOfficials_response_branch;

@Mapper
public interface ReportMapper_HigherOfficials {
  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_is_mgt"
  )
  List<ReportBranch_responseHigherOfficials_response_is_mgt> fetchReportHigherOfficials_is_mgt(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_cheque_printed_not_delivered_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_cheque_printed_not_delivered_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_actual_performance_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_actual_performance_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_cash_performance_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_cash_performance_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_cash_holding_limit_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_cash_holding_limit_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_cash_count_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_cash_count_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_age_of_item_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_age_of_item_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_supplies_stock_account_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_supplies_stock_account_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_staff_loan_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_staff_loan_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_loan_and_advance_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_loan_and_advance_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_accrued_interest_reciviable_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_accrued_interest_reciviable_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_account_balance_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_account_balance_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_controllable_expense_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_controllable_expense_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_deposit_account_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_deposit_account_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_vouching_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_vouching_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_insufficient_cheque_inspection"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_insufficient_cheque_inspection(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_cheque_delivered_not_sign"
  )
  ReportBranch_responseHigherOfficials fetchReportHigherOfficials_cheque_delivered_not_sign(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_atm"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_atm(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_incomplete"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_incomplete(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_operational"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_operational(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_dormant"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_dormant(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

  //  @SelectProvider(
  //   type = SqlProviderHigherOfficials.class,
  //   method = "fetchReportHigherOfficials_branch_observation"
  // )
  // List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_observation(
  //   @Param("region_id") Long region_id,
  //   @Param("branch_id") Long branch_id,
  //   @Param("module_type") String module_type,
  //   @Param("risk_level") String risk_level,
  //   @Param("amount_min") Double amount_min,
  //   @Param("amount_max") Double amount_max,
  //   @Param("user_id") Long user_id,
  //   @Param("user_roles") String[] user_roles
  // );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_abnormal"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_abnormal(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_asset_liability"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_asset_liability(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_controllable"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_controllable(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_suspense"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_suspense(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_memo"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_memo(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_negotiable"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_negotiable(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_long"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_long(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_loan"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_loan(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_cash_performance"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_cash_performance(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_cash_mgt"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_cash_mgt(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );

   @SelectProvider(
    type = SqlProviderHigherOfficials.class,
    method = "fetchReportHigherOfficials_branch_cash_count"
  )
  List<ReportHigherOfficials_response_branch> fetchReportHigherOfficials_branch_cash_count(
    @Param("region_id") Long region_id,
    @Param("branch_id") Long branch_id,
    @Param("module_type") String module_type,
    @Param("risk_level") String risk_level,
    @Param("amount_min") Double amount_min,
    @Param("amount_max") Double amount_max,
    @Param("user_id") Long user_id,
    @Param("user_roles") String[] user_roles
  );
}
