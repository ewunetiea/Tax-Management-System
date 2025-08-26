package com.afr.fms.Common.Dashboard.Higher_Officials;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForPieChart_byModule;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForStatChartE;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.TotalsForTimeSeriesModuleSpecificE;
import com.afr.fms.Common.Dashboard.Higher_Officials.Models.UnrectifiedForAllModulesE;

@Mapper
public interface DashboardMapper_HigherOfficials {
  @Select(
    "select" +
    "((select count(*) from branch_financial_audit where status = 1) +" +
    "(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where ida.status = 1) +" +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ima.status = 1)) as reported," +
    "" +
    "((select count(*) from branch_financial_audit where approve_status = 1 and status = 1) +" +
    "(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where ida.approve_status = 1 and ida.status = 1) +" +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ima.approve_status = 1 and ima.status = 1)) as approved," +
    "" +
    "((select count(*) from branch_financial_audit where rectification_status = 1 and status = 1) +" +
    "(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where ia.rectification_status = 1 and ida.status = 1) +" +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ismgta.rectification_status = 1 and ima.status = 1)) as rectified," +
    "" +
    "((select count(*) from branch_financial_audit where rectification_status = 0 and status = 1 and auditor_status = 1) +" +
    "(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where (ia.rectification_status is null or ia.rectification_status = 2 or ia.rectification_status = 0) and ida.status = 1) +" +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1)) as unrectified" +
    ""
  )
  public List<TotalsForStatChartE> getTotalsForStatChartE();

  @Select(
    "select 'IS' as title, (select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'IS' and ima.risk_level = 'Very Low') as very_low," +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'IS' and ima.risk_level = 'Low') as low," +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'IS' and ima.risk_level = 'Medium') as medium," +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'IS' and ima.risk_level = 'High') as high," +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'IS' and ima.risk_level = 'Very High') as very_high"
  )
  public UnrectifiedForAllModulesE getUnrectifiedForAllModules_is();

  @Select(
    "select 'INSPECTION' as title, (select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where (ia.rectification_status is null or ia.rectification_status = 2 or ia.rectification_status = 0) and ida.status = 1 and ida.risk_level = 'Very Low') as very_low, " +
    "(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where (ia.rectification_status is null or ia.rectification_status = 2 or ia.rectification_status = 0) and ida.status = 1 and ida.risk_level = 'Low') as low, " +
    "(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where (ia.rectification_status is null or ia.rectification_status = 2 or ia.rectification_status = 0) and ida.status = 1 and ida.risk_level = 'Medium') as medium, " +
    "(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where (ia.rectification_status is null or ia.rectification_status = 2 or ia.rectification_status = 0) and ida.status = 1 and ida.risk_level = 'High') as high, " +
    "(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where (ia.rectification_status is null or ia.rectification_status = 2 or ia.rectification_status = 0) and ida.status = 1 and ida.risk_level = 'Very High') as very_high"
  )
  public UnrectifiedForAllModulesE getUnrectifiedForAllModules_inspection();

  @Select(
    "select 'BRANCH FINANCIAL' as title, (select 0) as very_low, (select 0) as low, (select 0) as medium, (select count(*) from branch_financial_audit where rectification_status = 0 and status = 1 and auditor_status = 1) as high, (select 0) as very_high"
  )
  public UnrectifiedForAllModulesE getUnrectifiedForAllModules_branch();

  @Select(
    "select 'MANAGEMENT' as title, (select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'MGT' and ima.risk_level = 'Very Low') as very_low," +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'MGT' and ima.risk_level = 'Low') as low," +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'MGT' and ima.risk_level = 'Medium') as medium," +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'MGT' and ima.risk_level = 'High') as high," +
    "(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where (ismgta.rectification_status is null or ismgta.rectification_status = 2 or ismgta.rectification_status = 0) and ima.status = 1 and ima.category = 'MGT' and ima.risk_level = 'Very High') as very_high"
  )
  public UnrectifiedForAllModulesE getUnrectifiedForAllModules_mgt();

  @Select(
    "	SELECT " +
    "		reported_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY reported_timestamp) AS reported_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ima.finding_date as date) as reported_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ima.status = 1 and ima.category = 'IS'" +
    "		GROUP BY cast(ima.finding_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY reported_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_reportedE();

  @Select(
    "	SELECT " +
    "		approved_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY approved_timestamp) AS approved_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ima.approved_date as date) as approved_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ima.approve_status = 1 and ima.status = 1 and ima.category = 'IS'" +
    "		GROUP BY cast(ima.approved_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY approved_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_approvedE();

  @Select(
    "	SELECT " +
    "		rectified_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY rectified_timestamp) AS rectified_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ima.rectification_date as date) as rectified_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ismgta.rectification_status = 1 and ima.status = 1 and ima.category = 'IS'" +
    "		GROUP BY cast(ima.rectification_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY rectified_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_rectifiedE();

  @Select(
    "	SELECT " +
    "		unrectified_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY unrectified_timestamp) AS unrectified_value" +
    "	FROM (" +
    "		SELECT " +
    "			CASE " +
    "				WHEN ismgta.rectification_status = 2 THEN CAST(ismgta.unrectification_date AS DATE)" +
    "				WHEN ismgta.complete_status = 1 THEN CAST(ismgta.completed_date AS DATE)" +
    "			END AS unrectified_timestamp," +
    "			COUNT(*) AS DailyRecordCount" +
    "		FROM IS_Management_Audit ima" +
    "		INNER JOIN IS_MGT_Auditee ismgta ON ima.id = ismgta.IS_MGT_id" +
    "		WHERE (((ismgta.rectification_status IS NULL OR ismgta.rectification_status = 0) AND ismgta.complete_status = 1) OR ismgta.rectification_status = 2)" +
    "			AND ima.status = 1" +
    "			AND ima.category = 'is'" +
    "		GROUP BY " +
    "			CASE " +
    "				WHEN ismgta.rectification_status = 2 THEN CAST(ismgta.unrectification_date AS DATE)" +
    "				WHEN ismgta.complete_status = 1 THEN CAST(ismgta.completed_date AS DATE)" +
    "			END," +
    "			ismgta.rectification_status," +
    "			ismgta.completed_date" +
    "	) AS AggregatedData" +
    "	ORDER BY unrectified_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesIs_unrectifiedE();

  @Select(
    "	SELECT " +
    "		reported_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY reported_timestamp) AS reported_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ima.finding_date as date) as reported_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ima.status = 1 and ima.category = 'MGT'" +
    "		GROUP BY cast(ima.finding_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY reported_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_reportedE();

  @Select(
    "	SELECT " +
    "		approved_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY approved_timestamp) AS approved_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ima.approved_date as date) as approved_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ima.approve_status = 1 and ima.status = 1 and ima.category = 'MGT'" +
    "		GROUP BY cast(ima.approved_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY approved_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_approvedE();

  @Select(
    "	SELECT " +
    "		rectified_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY rectified_timestamp) AS rectified_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ima.rectification_date as date) as rectified_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ismgta.rectification_status = 1 and ima.status = 1 and ima.category = 'MGT'" +
    "		GROUP BY cast(ima.rectification_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY rectified_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_rectifiedE();

  @Select(
    "	SELECT " +
    "		unrectified_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY unrectified_timestamp) AS unrectified_value" +
    "	FROM (" +
    "		SELECT " +
    "			CASE " +
    "				WHEN ismgta.rectification_status = 2 THEN CAST(ismgta.unrectification_date AS DATE)" +
    "				WHEN ismgta.complete_status = 1 THEN CAST(ismgta.completed_date AS DATE)" +
    "			END AS unrectified_timestamp," +
    "			COUNT(*) AS DailyRecordCount" +
    "		FROM IS_Management_Audit ima" +
    "		INNER JOIN IS_MGT_Auditee ismgta ON ima.id = ismgta.IS_MGT_id" +
    "		WHERE (((ismgta.rectification_status IS NULL OR ismgta.rectification_status = 0) AND ismgta.complete_status = 1) OR ismgta.rectification_status = 2)" +
    "			AND ima.status = 1" +
    "			AND ima.category = 'mgt'" +
    "		GROUP BY " +
    "			CASE " +
    "				WHEN ismgta.rectification_status = 2 THEN CAST(ismgta.unrectification_date AS DATE)" +
    "				WHEN ismgta.complete_status = 1 THEN CAST(ismgta.completed_date AS DATE)" +
    "			END," +
    "			ismgta.rectification_status," +
    "			ismgta.completed_date" +
    "	) AS AggregatedData" +
    "	ORDER BY unrectified_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesMgt_unrectifiedE();

  @Select(
    "	SELECT " +
    "		reported_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY reported_timestamp) AS reported_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ida.inspection_date as date) as reported_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where ida.status = 1" +
    "		GROUP BY cast(ida.inspection_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY reported_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_reportedE();

  @Select(
    "	SELECT " +
    "		approved_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY approved_timestamp) AS approved_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ida.approved_date as date) as approved_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where ida.approve_status = 1 and ida.status = 1" +
    "		GROUP BY cast(ida.approved_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY approved_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_approvedE();

  @Select(
    "	SELECT " +
    "		rectified_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY rectified_timestamp) AS rectified_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(ia.rectification_date as date) as rectified_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where ia.rectification_status = 1 and ida.status = 1" +
    "		GROUP BY cast(ia.rectification_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY rectified_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_rectifiedE();

  @Select(
    "	SELECT " +
    "		unrectified_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY unrectified_timestamp) AS unrectified_value" +
    "	FROM (" +
    "		SELECT " +
    "			CASE " +
    "				WHEN ia.rectification_status = 2 THEN CAST(ia.unrectification_date AS DATE)" +
    "				WHEN ia.complete_status = 1 THEN CAST(ia.completed_date AS DATE)" +
    "			END AS unrectified_timestamp," +
    "			COUNT(*) AS DailyRecordCount" +
    "		FROM inspection_directorate_audit ida" +
    "		INNER JOIN inspection_auditee ia on ida.id = ia.inspection_id" +
    "		WHERE (((ia.rectification_status IS NULL OR ia.rectification_status = 0) AND ia.complete_status = 1) OR ia.rectification_status = 2)" +
    "			AND ida.status = 1" +
    "		GROUP BY " +
    "			CASE " +
    "				WHEN ia.rectification_status = 2 THEN CAST(ia.unrectification_date AS DATE)" +
    "				WHEN ia.complete_status = 1 THEN CAST(ia.completed_date AS DATE)" +
    "			END," +
    "			ia.rectification_status," +
    "			ia.completed_date" +
    "	) AS AggregatedData" +
    "	ORDER BY unrectified_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesInspection_unrectifiedE();

  @Select(
    "	SELECT " +
    "		reported_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY reported_timestamp) AS reported_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(finding_date as date) as reported_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM branch_financial_audit where status = 1" +
    "		GROUP BY cast(finding_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY reported_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_reportedE();

  @Select(
    "	SELECT " +
    "		approved_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY approved_timestamp) AS approved_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(approved_date as date) as approved_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM branch_financial_audit where approve_status = 1 and status = 1" +
    "		GROUP BY cast(approved_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY approved_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_approvedE();

  @Select(
    "	SELECT " +
    "		rectified_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY rectified_timestamp) AS rectified_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(rectification_date as date) as rectified_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM branch_financial_audit where rectification_status = 1 and status = 1" +
    "		GROUP BY cast(rectification_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY rectified_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_rectifiedE();

  @Select(
    "	SELECT " +
    "		unrectified_timestamp," +
    "		SUM(DailyRecordCount) OVER (ORDER BY unrectified_timestamp) AS unrectified_value" +
    "	FROM (" +
    "		 SELECT " +
    "			cast(passed_date as date) as unrectified_timestamp," +
    "			count(*) AS DailyRecordCount" +
    "		FROM branch_financial_audit where rectification_status = 0 and auditor_status = 1 and status = 1" +
    "		GROUP BY cast(passed_date as date)" +
    "	) AS AggregatedData" +
    "	ORDER BY unrectified_timestamp;"
  )
  public List<TotalsForTimeSeriesModuleSpecificE> getTotalsForTimeSeriesBranch_unrectifiedE();

  @Select("select " +
  		"	(select count(*) from branch_financial_audit where status = 1) as branch," +
  		"	(select count(*) from inspection_directorate_audit ida inner join inspection_auditee ia on ida.id = ia.inspection_id where ida.status = 1) as inspection," +
  		"	(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ima.status = 1 and category = 'is') as mgt," +
  		"	(select count(*) from IS_Management_Audit ima inner join IS_MGT_Auditee ismgta on ima.id = ismgta.IS_MGT_id where ima.status = 1 and category = 'mgt') as is_")
  public List<TotalsForPieChart_byModule> getTotalsForPieChart_byModule();
}
