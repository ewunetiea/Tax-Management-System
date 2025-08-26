package com.afr.fms.Followup_officer.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@Mapper
public interface FollowupOfficerMGTEnhancedMapper {

	// @Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as
	// directorate_id, br.name as auditee_name, isma.completed_date as
	// auditee_responded_date, isma.complete_status as auditee_responded_added from
	// IS_Management_Audit ism"
	// + " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and
	// isma.complete_status = 1 and isma.rectification_status = 0 "
	// + " inner join Branch br on br.id = isma.auditee_id"
	// + " where ism.category = #{category} and ism.audit_type = #{audit_type} and
	// ism.auditor_id = #{id} and ism.status = 1 Order By isma.completed_date DESC
	// ")

	@Select("<script>" +
			"SELECT ism.*, " +
			"       isma.id as auditee_id, " +
			"       isma.auditee_id as directorate_id, " +
			"       br.name as auditee_name, " +
			"       isma.completed_date as auditee_responded_date, " +
			"       isma.complete_status as auditee_responded_added " +
			"FROM IS_Management_Audit ism " +
			"INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id " +
			"    AND isma.complete_status = 1 " +
			"    AND isma.rectification_status = 0 " +
			"INNER JOIN Branch br ON br.id = isma.auditee_id " +
			"<choose>" +
			"   <when test='audit_type == \"abnormalBalance\"'> " +
			"       INNER JOIN abnormal_balance_mgt abm ON abm.is_management_audit_id = ism.id " +
			"   </when> " +
			"    <when test='audit_type == \"CashCount\"'>" +
			"        INNER JOIN cash_count_mgt ccm on ism.id  = ccm.is_mgt_audit_id " +
			"    </when>" +
			"    <when test='audit_type == \"LongOutstandingItems\"'>" +
			"            INNER JOIN long_outstanding_item_mgt lomi on ism.id = lomi.mgt_audit_id " +
			"        </when> " +
			"   <when test='audit_type == \"suspenseAccount\"'> " +
			"       INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ism.id " +
			"   </when> " +
			"   <when test='audit_type == \"creditDocumentation\"'> " +
			"       INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ism.id " +
			"   </when> " +
			"   <when test='audit_type == \"assetLiability\"'> " +
			"       INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ism.id " +
			"   </when> " +
			"   <when test='audit_type == \"memorandomContingent\"'> " +
			"       INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ism.id " +
			"   </when> " +
			"</choose>" +
			"WHERE ism.category = #{category} " +
			"  AND ism.audit_type = #{audit_type} " +
			"  AND ism.status = 1 " +
			"ORDER BY isma.completed_date DESC" +
			"</script>")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getAuditsForFollowupOfficerMGT(PaginatorPayLoad paginatorPayLoad);

	// @Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as
	// directorate_id, br.name as auditee_name, isma.completed_date as
	// auditee_responded_date, isma.complete_status as auditee_responded_added from
	// IS_Management_Audit ism"
	// + " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and
	// isma.complete_status = 1 and isma.rectification_status = 0 "
	// + " inner join Branch br on br.id = isma.auditee_id"
	// + " where ism.category = #{category} and ism.audit_type = #{audit_type} and
	// ism.status = 1 Order By isma.completed_date DESC ")

	@Select("<script>" +
			"SELECT ism.*, " +
			"       isma.id AS auditee_id, " +
			"       isma.auditee_id AS directorate_id, " +
			"       br.name AS auditee_name, " +
			"       isma.completed_date AS auditee_responded_date, " +
			"       isma.complete_status AS auditee_responded_added " +
			"FROM IS_Management_Audit ism " +
			"INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id " +
			"    AND isma.complete_status = 1 " +
			"    AND isma.rectification_status = 0 " +
			"INNER JOIN Branch br ON br.id = isma.auditee_id " +
			"<choose> " +
			"   <when test='audit_type == \"abnormalBalance\"'> " +
			"       INNER JOIN abnormal_balance_mgt abm ON abm.is_management_audit_id = ism.id " +
			"   </when> " +
			"    <when test='audit_type == \"CashCount\"'>" +
			"        INNER JOIN cash_count_mgt ccm on ism.id  = ccm.is_mgt_audit_id " +
			"    </when>" +
			"    <when test='audit_type == \"LongOutstandingItems\"'>" +
			"            INNER JOIN long_outstanding_item_mgt lomi on ism.id = lomi.mgt_audit_id " +
			"        </when> " +
			"   <when test='audit_type == \"suspenseAccount\"'> " +
			"       INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ism.id " +
			"   </when> " +
			"   <when test='audit_type == \"creditDocumentation\"'> " +
			"       INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ism.id " +
			"   </when> " +
			"   <when test='audit_type == \"assetLiability\"'> " +
			"       INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ism.id " +
			"   </when> " +
			"   <when test='audit_type == \"memorandomContingent\"'> " +
			"       INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ism.id " +
			"   </when> " +
			"</choose> " +
			"WHERE ism.category = #{category} " +
			"  AND ism.audit_type = #{audit_type} " +
			"  AND ism.status = 1 " +
			"ORDER BY isma.completed_date DESC" +
			"</script>")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getAuditsForFollowupOfficerIS(PaginatorPayLoad paginatorPayLoad);

	// @Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as
	// directorate_id, br.name as auditee_name, isma.rectification_date as
	// auditee_rectification_date, isma.completed_date as auditee_responded_date, "
	// + " isma.rectification_status as auditee_rectification_status,
	// isma.division_assigned as auditee_division_assigned, isma.complete_status as
	// auditee_responded_added from IS_Management_Audit ism"
	// + " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and
	// isma.rectification_status = 4 "
	// + " inner join Branch br on br.id = isma.auditee_id"
	// + " where ism.followup_id = #{user_id} and ism.audit_type = #{audit_type} and
	// ism.status = 1 Order By rectification_date DESC ")

	@Select("<script>"
			+ "SELECT ism.*, "
			+ "isma.id AS auditee_id, "
			+ "isma.auditee_id AS directorate_id, "
			+ "br.name AS auditee_name, "
			+ "isma.rectification_date AS auditee_rectification_date, "
			+ "isma.completed_date AS auditee_responded_date, "
			+ "isma.rectification_status AS auditee_rectification_status, "
			+ "isma.division_assigned AS auditee_division_assigned, "
			+ "isma.complete_status AS auditee_responded_added "
			+ "FROM IS_Management_Audit ism "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id AND isma.rectification_status = 4 "
			+ "INNER JOIN Branch br ON br.id = isma.auditee_id "

			+ "<choose> "
			+ "   <when test='audit_type == \"abnormalBalance\"'> "
			+ "       INNER JOIN abnormal_balance_mgt abm ON abm.is_management_audit_id = ism.id "
			+ "   </when> " +
			"    <when test='audit_type == \"CashCount\"'>" +
			"        INNER JOIN cash_count_mgt ccm on ism.id  = ccm.is_mgt_audit_id " +
			"    </when>" +
			"    <when test='audit_type == \"LongOutstandingItems\"'>" +
			"            INNER JOIN long_outstanding_item_mgt lomi on ism.id = lomi.mgt_audit_id " +
			"        </when> "
			+ "   <when test='audit_type == \"suspenseAccount\"'> "
			+ "       INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"creditDocumentation\"'> "
			+ "       INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"assetLiability\"'> "
			+ "       INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"memorandomContingent\"'> "
			+ "       INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "</choose>"
			+ "WHERE ism.followup_id = #{user_id} AND ism.audit_type = #{audit_type} AND ism.status = 1 "
			+ "ORDER BY ism.rectification_date DESC "
			+ "</script>")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getPartiallyRectifiedFindings(PaginatorPayLoad paginatorPayLoad);

	// @Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as
	// directorate_id, br.name as auditee_name, isma.division_assigned as
	// auditee_division_assigned, "
	// + " isma.complete_status as auditee_responded_added, "
	// + " isma.unrectification_date as auditee_unrectification_date,
	// isma.completed_date as auditee_responded_date from IS_Management_Audit ism"
	// + " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and
	// isma.rectification_status = 2 "
	// + " inner join Branch br on br.id = isma.auditee_id"
	// + " where ism.followup_id = #{user_id} and ism.audit_type = #{audit_type} and
	// ism.status = 1 Order By isma.unrectification_date DESC ")

	@Select("<script>"
			+ "SELECT ism.*, "
			+ "isma.id AS auditee_id, "
			+ "isma.auditee_id AS directorate_id, "
			+ "br.name AS auditee_name, "
			+ "isma.division_assigned AS auditee_division_assigned, "
			+ "isma.complete_status AS auditee_responded_added, "
			+ "isma.unrectification_date AS auditee_unrectification_date, "
			+ "isma.completed_date AS auditee_responded_date "
			+ "FROM IS_Management_Audit ism "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id AND isma.rectification_status = 2 "
			+ "INNER JOIN Branch br ON br.id = isma.auditee_id "

			+ "<choose> "
			+ "   <when test='audit_type == \"abnormalBalance\"'> "
			+ "       INNER JOIN abnormal_balance_mgt abm ON abm.is_management_audit_id = ism.id "
			+ "   </when> " +
			"    <when test='audit_type == \"CashCount\"'>" +
			"        INNER JOIN cash_count_mgt ccm on ism.id  = ccm.is_mgt_audit_id " +
			"    </when>" +
			"    <when test='audit_type == \"LongOutstandingItems\"'>" +
			"            INNER JOIN long_outstanding_item_mgt lomi on ism.id = lomi.mgt_audit_id " +
			"        </when> "
			+ "   <when test='audit_type == \"suspenseAccount\"'> "
			+ "       INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"creditDocumentation\"'> "
			+ "       INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"assetLiability\"'> "
			+ "       INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"memorandomContingent\"'> "
			+ "       INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "</choose>"
			+ "WHERE ism.followup_id = #{user_id} AND ism.audit_type = #{audit_type} AND ism.status = 1 "
			+ "ORDER BY isma.unrectification_date DESC "
			+ "</script>")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getRejectedFindings(PaginatorPayLoad PaginatorPayLoad);

	// @Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as
	// directorate_id, br.name as auditee_name, isma.completed_date as
	// auditee_responded_date, isma.complete_status as auditee_responded_added, "
	// + " isma.rectification_date as auditee_rectification_date from
	// IS_Management_Audit ism"
	// + " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and
	// isma.rectification_status = 1 "
	// + " inner join Branch br on br.id = isma.auditee_id"
	// + " where ism.followup_id = #{user_id} and ism.audit_type = #{audit_type} and
	// ism.status = 1 Order By isma.rectification_date DESC ")

	@Select("<script>"
			+ "SELECT ism.*, "
			+ "isma.id AS auditee_id, "
			+ "isma.auditee_id AS directorate_id, "
			+ "br.name AS auditee_name, "
			+ "isma.completed_date AS auditee_responded_date, "
			+ "isma.complete_status AS auditee_responded_added, "
			+ "isma.rectification_date AS auditee_rectification_date "
			+ "FROM IS_Management_Audit ism "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id AND isma.rectification_status = 1 "
			+ "INNER JOIN Branch br ON br.id = isma.auditee_id "
			+ "<choose> "
			+ "   <when test='audit_type == \"abnormalBalance\"'> "
			+ "       INNER JOIN abnormal_balance_mgt abm ON abm.is_management_audit_id = ism.id "
			+ "   </when> " +
			"    <when test='audit_type == \"CashCount\"'>" +
			"        INNER JOIN cash_count_mgt ccm on ism.id  = ccm.is_mgt_audit_id " +
			"    </when>" +
			"    <when test='audit_type == \"LongOutstandingItems\"'>" +
			"            INNER JOIN long_outstanding_item_mgt lomi on ism.id = lomi.mgt_audit_id " +
			"        </when> "
			+ "   <when test='audit_type == \"suspenseAccount\"'> "
			+ "       INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"creditDocumentation\"'> "
			+ "       INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"assetLiability\"'> "
			+ "       INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"memorandomContingent\"'> "
			+ "       INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "</choose>"
			+ "WHERE ism.followup_id = #{user_id} AND ism.audit_type = #{audit_type} AND ism.status = 1 "
			+ "ORDER BY isma.rectification_date DESC "
			+ "</script>")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getRectifiedFindings(PaginatorPayLoad paginatorPayLoad);

	// @Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as
	// directorate_id, br.name as auditee_name, isma.division_assigned as
	// auditee_division_assigned, "
	// + " isma.complete_status as auditee_responded_added, "
	// + " isma.unrectification_date as auditee_unrectification_date,
	// isma.completed_date as auditee_responded_date from IS_Management_Audit ism"
	// + " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and
	// isma.rectification_status = 2 "
	// + " inner join Branch br on br.id = isma.auditee_id"
	// + " where ism.followup_id = #{user_id} and ism.audit_type = #{audit_type} and
	// ism.status = 1 Order By isma.unrectification_date DESC ")

	@Select("<script>"
			+ "SELECT ism.*, "
			+ "isma.id AS auditee_id, "
			+ "isma.auditee_id AS directorate_id, "
			+ "br.name AS auditee_name, "
			+ "isma.division_assigned AS auditee_division_assigned, "
			+ "isma.complete_status AS auditee_responded_added, "
			+ "isma.unrectification_date AS auditee_unrectification_date, "
			+ "isma.completed_date AS auditee_responded_date "
			+ "FROM IS_Management_Audit ism "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id AND isma.rectification_status = 2 "
			+ "INNER JOIN Branch br ON br.id = isma.auditee_id "

			+ "<choose> "
			+ "   <when test='audit_type == \"abnormalBalance\"'> "
			+ "       INNER JOIN abnormal_balance_mgt abm ON abm.is_management_audit_id = ism.id "
			+ "   </when> " +
			"    <when test='audit_type == \"CashCount\"'>" +
			"        INNER JOIN cash_count_mgt ccm on ism.id  = ccm.is_mgt_audit_id " +
			"    </when>" +
			"    <when test='audit_type == \"LongOutstandingItems\"'>" +
			"            INNER JOIN long_outstanding_item_mgt lomi on ism.id = lomi.mgt_audit_id " +
			"        </when> "
			+ "   <when test='audit_type == \"suspenseAccount\"'> "
			+ "       INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"creditDocumentation\"'> "
			+ "       INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"assetLiability\"'> "
			+ "       INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "   <when test='audit_type == \"memorandomContingent\"'> "
			+ "       INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ism.id "
			+ "   </when> "
			+ "</choose>"
			+ "WHERE ism.followup_id = #{user_id} AND ism.audit_type = #{audit_type} AND ism.status = 1 "
			+ "ORDER BY isma.unrectification_date DESC "
			+ "</script>")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getUnrectifiedFindings(PaginatorPayLoad paginatorPayLoad);

	// Full Rectification Status
	@Update("update IS_Management_Audit set rectification_status = 1 , finding_status = 'Fully Rectified', followup_id= #{followup_officer.id}, rectification_date = CURRENT_TIMESTAMP where id = #{id}")
	public void rectifyISMFindings(AuditISM auditISM);

	@Update("update IS_MGT_Auditee set rectification_status = 1 , finding_status = 'Fully Rectified', rectification_date = CURRENT_TIMESTAMP where id = #{id}")
	public void rectifyFindingsAuditee(IS_MGT_Auditee is_MGT_Auditee);

	@Update("update IS_Management_Audit set rectification_status = 2, finding_status= 'Unrectified', unrectification_date = CURRENT_TIMESTAMP, followup_id= #{followup_officer.id}  where id = #{id}")
	public void unRectifyISMFindings(AuditISM audit);

	@Update("update IS_Management_Audit set rectification_status = 2, followup_id = #{followup_officer.id}, unrectification_date = CURRENT_TIMESTAMP, finding_status= 'Unrectified'  where id = #{id}")
	public void rejectISMFinding(AuditISM audit);

	@Update("update IS_MGT_Auditee set rectification_status = 2, unrectification_date = CURRENT_TIMESTAMP, finding_status= 'Unrectified'  where id = #{id}")
	public void unrectifyFindingAuditee(IS_MGT_Auditee is_MGT_Auditee);

	// Partially Rectification Status
	@Update("update IS_Management_Audit set rectification_status = 4, finding_status= 'Partially Rectified', rectification_date = CURRENT_TIMESTAMP, followup_id= #{followup_officer.id}  where id = #{id}")
	public void partiallyRectifyISMFindings(AuditISM audit);

	@Update("update IS_MGT_Auditee set rectification_status = 4, finding_status= 'Partially Rectified', rectification_date = CURRENT_TIMESTAMP where id = #{id}")
	public void partiallyRectifyFindingsAuditee(IS_MGT_Auditee is_MGT_Auditee);

	@Update("update IS_MGT_Auditee set complete_status = 0 where id=#{auditee_id}")
	public void updateIS_MGT_Auditee(Long auditee_id);

	@Update("update Audtiee_Division_ISM set submitted_auditee = 0, auditee_submitted = 0 where id=#{auditeeDivision_id}")
	public void updateAuditeeDivision(Long auditeeDivision_id);
}
