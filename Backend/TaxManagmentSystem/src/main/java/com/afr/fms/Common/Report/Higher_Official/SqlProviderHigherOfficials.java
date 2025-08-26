package com.afr.fms.Common.Report.Higher_Official;

import java.util.Arrays;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class SqlProviderHigherOfficials {

  public String fetchReportHigherOfficials_branch_incomplete(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.account_opened_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as account_opened_amount");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "incomplete_account_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_atm(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "   ISNULL(sum(acb.issued_card), 0) as issued_card,  " + //
            "   ISNULL(sum(acb.distributed_card), 0) as distributed_card,  " + //
            "   ISNULL(sum(acb.returned_card), 0) as returned_card,  " + //
            "   ISNULL(sum(acb.remaining_card), 0) as remaining_card");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN("ATM_card_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_operational(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        " cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as amount");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "operational_descripancy_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_abnormal(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.debit),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as debit,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.credit),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as credit");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "abnormal_balance_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_asset_liability(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.asset_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as asset_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.liability_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as liability_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "asset_liability_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_controllable(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.variation),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as variation,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.budget_per_plan),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as budget_per_plan,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.actual_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as actual_balance");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "controllable_expense_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_dormant(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as amount");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "dormant_inactive_account_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_suspense(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.balance_per_tracer),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as balance_per_tracer,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.balance_per_trial_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as balance_per_trial_balance");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "suspense_account_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_memo(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.memorandom_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as memorandom_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.contingent_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as contingent_amount");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "memorandom_contingent_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_negotiable(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "   ISNULL(sum(acb.quantity), 0) as quantity,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.unit_price),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as unit_price,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.trial_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as trial_balance");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "negotiable_instrument_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_long(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "   isnull(sum(ISNULL(acb.less_than_90_number, 0)) +  " + //
            "   sum(ISNULL(acb.greater_than_90_number, 0)) +  " + //
            "   sum(ISNULL(acb.greater_than_180_number, 0)) +  " + //
            "   sum(ISNULL(acb.greater_than_365_number, 0)), 0) as total_number,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.trial_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as trial_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.total_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as total_amount");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "long_outstanding_item_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_loan(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.amount_granted),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as amount_granted,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.arrears),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as arrears");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN("loan_advance_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_cash_performance(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.amount_shortage),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as amount_shortage,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.amount_excess),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as amount_excess");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "cash_performance_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_cash_mgt(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.cash_type, 'LCY'), ':', acb.average_cash_holding),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as average_cash_holding,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.cash_type, 'LCY'), ':', acb.branch_cash_set_limit),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as branch_cash_set_limit,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.cash_type, 'LCY'), ':', acb.mid_rate_fcy),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as mid_rate_fcy,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.cash_type, 'LCY'), ':', acb.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN(
            "cash_management_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_branch_cash_count(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    SQL sql = new SQL();

    sql.SELECT(
        "    cra.id AS id,  " + //
            "    dbo._StripHTML(cra.finding) AS finding,  " + //
            "    dbo._StripHTML(cra.impact) AS impact,  " + //
            "    dbo._StripHTML(cra.recommendation) AS recommendation,  " + //
            "    COUNT(*) AS total,  " + //
            "    dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.actual_cash_count),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as actual_cash_count,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.trial_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as trial_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.atm_amount_branch),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as atm_amount_branch,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.atm_amount_lobby),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as atm_amount_lobby,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference");
    sql.FROM("branch br");
    sql
        .FROM("branch_financial_audit AS bfa")
        .INNER_JOIN("cash_count_branch AS acb ON bfa.id = acb.branch_audit_id")
        .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
        .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
        .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id = car.compiled_id");
    sql
        .AND()
        .WHERE(
            "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1 and bfa.auditor_status = 1");
    sql.GROUP_BY(
        "cra.id,  " + //
            "    dbo._StripHTML(cra.finding),  " + //
            "    dbo._StripHTML(cra.impact),  " + //
            "    dbo._StripHTML(cra.recommendation)");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  // public String fetchReportHigherOfficials_branch_observation(
  // @Param("region_id") Long region_id,
  // @Param("branch_id") Long branch_id,
  // @Param("module_type") String module_type,
  // @Param("risk_level") String risk_level,
  // @Param("amount_min") Double amount_min,
  // @Param("amount_max") Double amount_max,
  // @Param("user_id") Long user_id,
  // @Param("user_roles") String[] user_roles
  // ) {
  // SQL sql = new SQL();

  // sql.SELECT(
  // " cra.id AS id, " + //
  // " dbo._StripHTML(cra.finding) AS finding, " + //
  // " dbo._StripHTML(cra.impact) AS impact, " + //
  // " dbo._StripHTML(cra.recommendation) AS recommendation, " + //
  // " COUNT(*) AS total, " + //
  // " dbo.SUMAccountTypeAMOUNTS( " + //
  // " STRING_AGG( " + //
  // " CONCAT(ISNULL(acb.fcy, 'LCY'), ':', acb.amount), " + //
  // " ',' " + //
  // " ) " + //
  // " ) as amount"
  // );
  // sql.FROM("branch br");
  // sql
  // .FROM("branch_financial_audit AS bfa")
  // .INNER_JOIN(
  // "dormant_inactive_account_branch AS acb ON bfa.id = acb.branch_audit_id"
  // )
  // .LEFT_OUTER_JOIN("compiled_audits AS ca ON bfa.id = ca.audit_id")
  // .LEFT_OUTER_JOIN("compiled_branch_audit AS cba ON ca.compiled_id = cba.id")
  // .LEFT_OUTER_JOIN("compiled_audits_region AS car ON car.audit_id = cba.id")
  // .LEFT_OUTER_JOIN("compiled_regional_audit AS cra ON cra.id =
  // car.compiled_id");
  // sql
  // .AND()
  // .WHERE(
  // "bfa.branch_id = br.id and bfa.rectification_status = 0 and bfa.status = 1
  // and bfa.auditor_status = 1"
  // );
  // sql.GROUP_BY(
  // "cra.id, " + //
  // " dbo._StripHTML(cra.finding), " + //
  // " dbo._StripHTML(cra.impact), " + //
  // " dbo._StripHTML(cra.recommendation)"
  // );
  // if (region_id != null) {
  // sql.AND().WHERE("br.region_id = #{region_id}");
  // }
  // if (branch_id != null) {
  // sql.AND().WHERE("br.id = #{branch_id}");
  // }
  // System.out.println("sql to string: " + sql.toString());
  // return sql.toString();
  // }

  public String fetchReportHigherOfficials_is_mgt(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();
    if (module_type.equalsIgnoreCase("IS") ||
        module_type.equalsIgnoreCase("Management")) {
      if (module_type.equalsIgnoreCase("IS"))
        sql.SELECT(
            "ima.id,  " + //
                "    dbo._StripHTML(ima.finding) as finding,  " + //
                "    dbo._StripHTML(ima.impact) as impact,  " + //
                "    dbo._StripHTML(ima.recommendation) as recommendation,  " + //
                "    ima.risk_level,  " + //
                "   br.name as auditee,  " + //
                "   STRING_AGG(dbo._StripHTML(adi.action_plan), ', ' ) AS auditee_response,  " + //
                "   cast(ima.finding_date as date) as finding_date");
      else
        sql.SELECT(
            "ima.id,  " + //
                "    dbo._StripHTML(ima.finding) as finding,  " + //
                "    dbo._StripHTML(ima.impact) as impact,  " + //
                "    dbo._StripHTML(ima.recommendation) as recommendation,  " + //
                "    ima.risk_level,  " + //
                "    ima.cash_type,  " + //
                "    ima.fcy,  " + //
                "   br.name as auditee,  " + //
                "   ima.amount,  " + //
                "   STRING_AGG(dbo._StripHTML(adi.action_plan), ', ' ) AS auditee_response,  " + //
                "   cast(ima.finding_date as date) as finding_date");
      sql.FROM("branch br");
      sql
          .FROM("IS_Management_Audit ima")
          .INNER_JOIN("IS_MGT_Auditee ismgta ON ima.id = ismgta.IS_MGT_id")
          .LEFT_OUTER_JOIN(
              "Audtiee_Division_ISM adi ON ismgta.id = adi.IS_MGT_Auditee_id");
      sql
          .AND()
          .WHERE(
              "((  " + //
                  "        (ismgta.rectification_status IS NULL OR ismgta.rectification_status = 0)  " + //
                  "        AND ima.approve_status = 1  " + //
                  "    )  " + //
                  "    OR ismgta.rectification_status = 2)");
      sql.AND().WHERE("ima.status = 1");
      sql.AND().WHERE("ismgta.auditee_id = br.id");
      if (module_type.equalsIgnoreCase("IS"))
        sql
            .AND()
            .WHERE("ima.category = 'is'");
      else
        sql
            .AND()
            .WHERE("ima.category = 'mgt'");
      if (region_id != null) {
        sql.AND().WHERE("br.region_id = #{region_id} OR ismgta.auditee_id = #{region_id}");
      }
      if (branch_id != null) {
        sql.AND().WHERE("br.id = #{branch_id}");
      }
      if (risk_level != null) {
        sql.AND().WHERE("ima.risk_level = #{risk_level}");
      }
      if (amount_min != null) {
        sql.AND().WHERE("ima.amount >= #{amount_min}");
      }
      if (amount_max != null) {
        sql.AND().WHERE("ima.amount <= #{amount_max}");
      }
      if (module_type.equalsIgnoreCase("IS"))
        sql.GROUP_BY(
            "   ima.id,  " + //
                "    dbo._StripHTML(ima.finding),  " + //
                "    dbo._StripHTML(ima.impact),  " + //
                "    dbo._StripHTML(ima.recommendation),  " + //
                "    ima.risk_level,  " + //
                "   br.name,  " + //
                "   ima.finding_date");
      else
        sql.GROUP_BY(
            "   ima.id,  " + //
                "    dbo._StripHTML(ima.finding),  " + //
                "    dbo._StripHTML(ima.impact),  " + //
                "    dbo._StripHTML(ima.recommendation),  " + //
                "    ima.risk_level,  " + //
                "    ima.cash_type,  " + //
                "    ima.fcy,  " + //
                "   br.name,  " + //
                "   ima.amount,  " + //
                "   ima.finding_date");
    }
    if (module_type.equalsIgnoreCase("Inspection")) {
    }
    if (module_type.equalsIgnoreCase("Branch Financial")) {
    }

    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_cheque_printed_not_delivered_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as cheque_printed_not_delivered_inspection_total,  " + //
            "    ISNULL(sum(dai.number_of_days_hold_by_branch), 0) as number_of_days_hold_by_branch");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .LEFT_OUTER_JOIN(
            "cheque_printed_not_delivered_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_actual_performance_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "count(*) as actual_performance_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', CASE WHEN dai.actual_performance_in_amount is not null THEN dai.actual_performance_in_amount ELSE 0 END),  "
            + //
            "            ','  " + //
            "        )  " + //
            "    ) as actual_performance_in_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':',  CASE WHEN dai.actual_performance_in_number is not null THEN dai.actual_performance_in_number ELSE 0 END),  "
            + //
            "            ','  " + //
            "        )  " + //
            "    ) as actual_performance_in_number,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':',  CASE WHEN dai.plan_in_amount is not null THEN dai.plan_in_amount ELSE 0 END),  "
            + //
            "            ','  " + //
            "        )  " + //
            "    ) as plan_in_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', CASE WHEN dai.plan_in_number  is not null THEN dai.plan_in_number  ELSE 0 END),  "
            + //
            "            ','  " + //
            "        )  " + //
            "    ) as plan_in_number,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':',  CASE WHEN dai.variation_in_amount  is not null THEN dai.variation_in_amount  ELSE 0 END),  "
            + //
            "            ','  " + //
            "        )  " + //
            "    ) as variation_in_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', CASE WHEN  dai.variation_in_number  is not null THEN  dai.variation_in_number  ELSE 0 END),  "
            + //
            "            ','  " + //
            "        )  " + //
            "    ) as variation_in_number");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "actual_performance_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_cash_performance_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "count(*) as cash_performance_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.last_year_shortage_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as last_year_shortage_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.last_year_shortage_frequency),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as last_year_shortage_frequency,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.last_year_excess_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as last_year_excess_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.last_year_excess_frequency),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as last_year_excess_frequency,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.recent_year_excess_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as recent_year_excess_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.recent_year_excess_frequency),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as recent_year_excess_frequency,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.recent_year_shortage_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as recent_year_shortage_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.recent_year_shortage_frequency),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as recent_year_shortage_frequency,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference_in_shortage),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference_in_shortage,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference_in_excess),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference_in_excess,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference_shortage_frequency),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference_shortage_frequency,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference_in_excess_frequency),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference_in_excess_frequency");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "cash_performance_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_cash_holding_limit_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "count(*) as cash_holding_limit_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.cash_holding_limit),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as cash_holding_limit,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.average_cash_limit),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as average_cash_limit,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "cash_holding_limit_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_cash_count_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "count(*) as cash_count_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.actual_cash_count),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as actual_cash_count,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.trial_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as trial_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "cash_count_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_age_of_item_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "count(*) as age_of_item_inspection_total,  " + //
            "   isnull(sum(COALESCE(dai.amount_of_item_for_greater_than_360_days, 0)) +  " + //
            "   sum(COALESCE(dai.amount_of_item_for_180_to_360_days, 0)) +  " + //
            "   sum(COALESCE(dai.amount_of_item_for_90_to_180_days, 0)) +  " + //
            "    sum(COALESCE(dai.amount_of_item_for_less_than_90_days, 0)), 0) as total_amount,  " + //
            "   isnull(sum(COALESCE(dai.number_of_item_for_less_than_90_days, 0)) +  " + //
            "   sum(COALESCE(dai.number_of_item_for_90_to_180_days, 0)) +  " + //
            "   sum(COALESCE(dai.number_of_item_for_180_to_360_days, 0)) +  " + //
            "   sum(COALESCE(dai.number_of_item_for_greater_than_360_days, 0)), 0) as total_number");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "age_of_item_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_supplies_stock_account_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as supplies_stock_account_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.number_of_checkbook),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as number_of_checkbook,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.unit_cost),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as unit_cost,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.trial_balance_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as trial_balance_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as amount");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "supplies_stock_account_inspection AS dai ON ida.id = dai.inspection_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_staff_loan_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as staff_loan_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.staff_loan_amount_as_per_schedule),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as staff_loan_amount_as_per_schedule,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.staff_loan_amount_as_per_trial_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as staff_loan_amount_as_per_trial_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "staff_loan_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_loan_and_advance_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "count(*) as loan_and_advance_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.loanAmount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as loan_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.outstandingBalance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as outstanding_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.arrearsAmount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as arrears_amount,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.fees_and_charges),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as fees_and_charges,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.interest_income),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as interest_income,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.penalty_charge),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as penalty_charge");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "loan_and_advance_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_accrued_interest_reciviable_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as accrued_interest_reciviable_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.trial_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as trial_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.accrual_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as accrual_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "accrued_interest_reciviable_inspection AS dai ON ida.id = dai.inspection_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_account_balance_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as account_balance_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.tracer_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as tracer_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.system_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as system_balance,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.difference),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as difference");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "account_balance_inspection AS dai ON ida.id = dai.inspection_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_controllable_expense_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as controllable_expense_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.previous_period_figure),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as previous_period_figure,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.current_period_figure),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as current_period_figure,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.variation),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as variation,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.percentage),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as percentage");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "controllable_expense_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_deposit_account_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as deposit_account_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.abnormal_balance),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as abnormal_balance");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "deposit_account_inspection AS dai ON ida.id = dai.inspection_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_vouching_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as vouching_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as amount");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "vouching_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_insufficient_cheque_inspection(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as insufficient_cheque_inspection_total,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.frequency),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as frequency,  " + //
            "   dbo.SUMAccountTypeAMOUNTS(  " + //
            "        STRING_AGG(  " + //
            "            CONCAT(ISNULL(dai.fcy, 'LCY'), ':', dai.cheque_amount),  " + //
            "            ','  " + //
            "        )  " + //
            "    ) as cheque_amount");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "insufficient_cheque_inspection AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }

  public String fetchReportHigherOfficials_cheque_delivered_not_sign(
      @Param("region_id") Long region_id,
      @Param("branch_id") Long branch_id,
      @Param("module_type") String module_type,
      @Param("risk_level") String risk_level,
      @Param("amount_min") Double amount_min,
      @Param("amount_max") Double amount_max,
      @Param("user_id") Long user_id,
      @Param("user_roles") String[] user_roles) {
    System.out.println("region_id: " + region_id);
    System.out.println("branch_id: " + branch_id);
    System.out.println("module_type: " + module_type);
    System.out.println("risk_level: " + risk_level);
    System.out.println("amount_min: " + amount_min);
    System.out.println("amount_max: " + amount_max);
    System.out.println("user_id: " + user_id);
    System.out.println("user_roles: " + Arrays.toString(user_roles));

    SQL sql = new SQL();

    sql.SELECT(
        "   count(*) as cheque_delivered_not_sign_total,  " + //
            "    ISNULL(sum(dai.number_of_days), 0) as number_of_days");
    sql.FROM("branch br");
    sql
        .FROM("inspection_directorate_audit ida")
        .INNER_JOIN(
            "cheque_delivered_not_sign AS dai ON ida.id = dai.inspection_audit_id")
        .INNER_JOIN("inspection_auditee AS ia ON ida.id = ia.inspection_id");
    sql
        .AND()
        .WHERE(
            "(ia.auditee_branch_id = br.id)  " + //
                "   AND (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ida.approve_status = 1) OR ia.rectification_status = 2)  "
                + //
                "   AND ida.status = 1");
    if (region_id != null) {
      sql.AND().WHERE("br.region_id = #{region_id} OR ia.auditee_region_id = #{region_id}");
    }
    if (branch_id != null) {
      sql.AND().WHERE("br.id = #{branch_id}");
    }
    if (risk_level != null) {
      sql.AND().WHERE("ida.risk_level = #{risk_level}");
    }
    System.out.println("sql to string: " + sql.toString());
    return sql.toString();
  }
}
