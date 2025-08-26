package com.afr.fms.Auditee.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Auditee.Entity.AuditeeDivisionFileISM;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;

@Mapper
public interface AuditeeISMEnhancedMapper {

	@Insert("insert into Audtiee_Division_ISM(division_id, IS_MGT_Auditee_id, division_assigned_date) values (#{division_id}, #{IS_MGT_Auditee_id}, CURRENT_TIMESTAMP)")
	public void add_auditee_division_ism(Long division_id, Long IS_MGT_Auditee_id);

	@Delete("delete from Audtiee_Division_ISM where division_id = #{division_id} and IS_MGT_Auditee_id =  #{IS_MGT_Auditee_id}")
	public void delete_auditee_division_ism(Long division_id, Long IS_MGT_Auditee_id);

	@Select("select * from Audtiee_Division_ISM where IS_MGT_Auditee_id = #{IS_MGT_Auditee_id} and submitted_auditee = 1")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAttachedFiles"))
	})
	public List<AuditeeDivisionISM> getAuditeeResponse(Long IS_MGT_Auditee_id);

	@Select("select * from Audtiee_Division_ISM where id = #{id} and submitted_auditee = 1")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAttachedFiles"))
	})
	public List<AuditeeDivisionISM> getAuditeeResponseById(Long id);

	@Select("select * from Audtiee_Division_ISM where IS_MGT_Auditee_id = #{IS_MGT_Auditee_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
	})
	public List<AuditeeDivisionISM> getAssignedAuditeeDivisions(Long IS_MGT_Auditee_id);

	@Select("select * from Audtiee_Division_ISM where IS_MGT_Auditee_id = #{IS_MGT_Auditee_id} and division_id = #{division_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
	})
	public AuditeeDivisionISM getAssignedAuditeeDivisionsByAuditeeandDivisionID(Long IS_MGT_Auditee_id,
			Long division_id);

	@Select("select * from Audtiee_Division_ISM where division_id = #{division_id} and IS_MGT_Auditee_id = #{IS_MGT_Auditee_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAttachedFiles"))
	})
	public List<AuditeeDivisionISM> getAuditeeResponseByAuditeeandDivisionID(Long division_id, Long IS_MGT_Auditee_id);

	@Select("select * from Audtiee_Division_ISM where id = #{auditee_division_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAttachedFiles"))
	})
	public AuditeeDivisionISM getAuditeeResponseISM(Long auditee_division_id);

	@Select("select * from Auditee_Division_File_ISM where auditee_division_id = #{auditee_division_id}")
	@Results(value = {
	})
	public List<AuditeeDivisionFileISM> getAttachedFiles(Long auditee_division_id);

	@Update("update IS_MGT_Auditee set complete_status = 1, completed_date = CURRENT_TIMESTAMP, finding_status = 'Responded' where id=#{auditee_id}")
	public void finishAuditeeResponse(Long auditee_id);

	@Select("<script>" +
			"WITH CTE AS (  " +
			"    SELECT ima.*, isma.id AS auditee_id,  " +
			"           ROW_NUMBER() OVER (PARTITION BY ima.case_number ORDER BY ima.approved_date) AS RowNum  " +
			"    FROM IS_Management_Audit ima  " +
			"    INNER JOIN  " +
			"        IS_MGT_Auditee isma ON ima.id = isma.IS_MGT_id AND isma.division_assigned = 0  and isma.self_response = 0 "
			+
			"    <choose> "
			+
			"        <when test='audit_type == \"AbnormalBalance\"'> " +
			"            INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id " +
			"        </when> " +
			"    <when test='audit_type == \"CashCount\"'>" +
			"        INNER JOIN cash_count_mgt ccm on ima.id = ccm.is_mgt_audit_id " +
			"    </when>" +
			"    <when test='audit_type == \"LongOutstandingItems\"'>" +
			"            INNER JOIN long_outstanding_item_mgt lomi on ima.id = lomi.mgt_audit_id " +
			"        </when> " +
			"        <when test='audit_type == \"SuspenseAccount\"'> " +
			"            INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ima.id " +
			"        </when> " +
			"        <when test='audit_type == \"creditDocumentation\"'> " +
			"            INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ima.id " +
			"        </when> " +
			"        <when test='audit_type == \"assetLiability\"'> " +
			"            INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ima.id " +
			"        </when> " +

			"        <when test='audit_type == \"memorandomContingent\"'> " +
			"            INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ima.id " +
			"        </when> " +
			"    </choose> " +
			"    WHERE isma.auditee_id = #{auditee_id} and ima.approve_status = 1 and ima.audit_type = #{audit_type} " +
			")  " +
			"SELECT * FROM CTE WHERE RowNum = 1 ORDER BY approved_date " +
			"</script>")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId"))
	})
	public List<AuditISM> getAuditsForAuditee(@Param("auditee_id") Long auditee_id,
			@Param("audit_type") String audit_type);

	@Select("select * from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where id = #{auditee_id})")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public AuditISM getAuditByAuditeeID(Long auditee_id);

	@Update("update Audtiee_Division_ISM set auditee_submitted = 1, auditee_submitted_date = CURRENT_TIMESTAMP  where id = #{division_id}")
	public void submitAuditAuditeeResponse(Long division_id);

	@Select("<script>" +
			"SELECT ima.*, "
			+ "adi.submitted_auditee AS division_response_added, "
			+ "adi.submitted_auditee AS divisionresponseadded, "
			+ "isma.division_assigned AS auditee_division_assigned, " +

			" CASE " +
			"    WHEN adi.division_id is NOT NULL THEN br.name   " +
			"    ELSE 'Self Response' " +
			"  END AS division_name, "
			+ "isma.id AS auditee_id, "
			+ "isma.auditee_id AS directorate_id, "
			+ "adi.id AS division_auditee_id, "
			+ "adi.response_subimtted_date AS division_response_date, "
			+ "isma.rectification_status AS auditee_rectification_status, "
			+ "isma.rectification_date AS auditee_rectification_date "
			+ "FROM IS_Management_Audit ima "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
			+ "AND isma.rectification_status = 4 "
			+ "AND isma.complete_status != 1 "
			+ "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
			+ "LEFT JOIN branch br ON br.id = adi.division_id " +

			"<choose> " +
			"   <when test='audit_type == \"AbnormalBalance\"'> " +
			"       INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id  " +
			"   </when> " +
			"    <when test='audit_type == \"CashCount\"'>" +
			"        INNER JOIN cash_count_mgt ccm on ima.id = ccm.is_mgt_audit_id " +
			"    </when>" +
			"    <when test='audit_type == \"LongOutstandingItems\"'>" +
			"            INNER JOIN long_outstanding_item_mgt lomi on ima.id = lomi.mgt_audit_id " +
			"        </when> " +
			"   <when test='audit_type == \"suspenseAccount\"'> " +
			"       INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ima.id " +
			"   </when> " +
			"   <when test='audit_type == \"creditDocumentation\"'> " +
			"       INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ima.id " +
			"   </when> " +
			"   <when test='audit_type == \"assetLiability\"'> " +
			"       INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ima.id " +
			"   </when> " +
			"   <when test='audit_type == \"memorandomContingent\"'> " +
			"       INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ima.id " +
			"   </when> " +
			"</choose> " +

			"WHERE isma.auditee_id = #{auditee_id} " +
			"  AND audit_type = #{audit_type} " +
			"ORDER BY adi.response_subimtted_date DESC" +
			"</script>")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId"))
	})
	public List<AuditISM> getPartiallyRectifiedAuditsForAuditee(@Param("auditee_id") Long auditee_id,
			@Param("audit_type") String audit_type);

	@Select({
			"<script>",
			"SELECT ima.*, "
					+ "adi.submitted_auditee AS division_response_added, "
					+ "adi.submitted_auditee AS divisionresponseadded, "
					+ "isma.division_assigned AS auditee_division_assigned, " +

					" CASE " +
					"    WHEN adi.division_id is NOT NULL THEN br.name   " +
					"    ELSE 'Self Response' " +
					"  END AS division_name, " +

					"isma.id AS auditee_id, "
					+ "adi.id AS division_auditee_id, isma.auditee_id as directorate_id, "
					+ "adi.response_subimtted_date AS division_response_date "
					+ "FROM IS_Management_Audit ima "
					+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
					+ "AND isma.rectification_status = 0 "
					+ "AND (isma.division_assigned = 1 or isma.self_response = 1)"
					+ "AND isma.complete_status != 1 "
					+ "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
					+ "LEFT JOIN branch br ON br.id = adi.division_id " +

					"<choose>" +
					"    <when test='audit_type == \"AbnormalBalance\"'>" +
					"        INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id  " +
					"    </when>" +
					"    <when test='audit_type == \"CashCount\"'>" +
					"        INNER JOIN cash_count_mgt ccm on ima.id = ccm.is_mgt_audit_id " +
					"    </when>" +
					"    <when test='audit_type == \"LongOutstandingItems\"'>" +
					"            INNER JOIN long_outstanding_item_mgt lomi on ima.id = lomi.mgt_audit_id " +
					"        </when> " +
					"    <when test='audit_type == \"suspenseAccount\"'>" +
					"        INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ima.id " +
					"    </when>" +
					"    <when test='audit_type == \"creditDocumentation\"'>" +
					"        INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ima.id " +
					"    </when>" +
					"    <when test='audit_type == \"assetLiability\"'>" +
					"        INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ima.id " +
					"    </when>" +
					"    <when test='audit_type == \"memorandomContingent\"'>" +
					"        INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ima.id " +
					"    </when>" +
					"</choose>" +

					"WHERE isma.auditee_id = #{auditee_id} " +
					"  AND ima.audit_type = #{audit_type} " +
					"ORDER BY adi.response_subimtted_date DESC" +
					"</script>"
	})
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId"))
	})
	public List<AuditISM> getAuditsForAuditeeOnProgress(@Param("auditee_id") Long auditee_id,
			@Param("audit_type") String audit_type);

	@Select({
			"<script>",
			"SELECT ima.*, "
					+ "adi.submitted_auditee AS division_response_added, "
					+ "adi.submitted_auditee AS divisionresponseadded, "
					+ "isma.division_assigned AS auditee_division_assigned, " +

					" CASE " +
					"    WHEN adi.division_id is NOT NULL THEN br.name   " +
					"    ELSE 'Self Response' " +
					"  END AS division_name, " +

					"adi.id AS division_auditee_id, "
					+ "isma.id AS auditee_id, "
					+ "isma.auditee_id AS directorate_id, "
					+ "adi.response_subimtted_date AS division_response_date, "
					+ "isma.rectification_status AS auditee_rectification_status, "
					+ "isma.unrectification_date AS auditee_unrectification_date "
					+ "FROM IS_Management_Audit ima "
					+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
					+ "AND isma.rectification_status = 2 "
					+ "AND isma.complete_status != 1 "
					+ "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
					+ "LEFT JOIN branch br ON br.id = adi.division_id " +

					// Dynamic join block using <choose>
					"<choose>" +
					"    <when test='audit_type == \"AbnormalBalance\"'>" +
					"        INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id  " +
					"    </when>" +
					"    <when test='audit_type == \"CashCount\"'>" +
					"        INNER JOIN cash_count_mgt ccm on ima.id = ccm.is_mgt_audit_id " +
					"    </when>" +
					"    <when test='audit_type == \"LongOutstandingItems\"'>" +
					"            INNER JOIN long_outstanding_item_mgt lomi on ima.id = lomi.mgt_audit_id " +
					"        </when> " +
					"    <when test='audit_type == \"suspenseAccount\"'>" +
					"        INNER JOIN suspense_account_mgt sam ON sam.is_mgt_audit_id = ima.id " +
					"    </when>" +
					"    <when test='audit_type == \"creditDocumentation\"'>" +
					"        INNER JOIN credit_documentation_parent_mgt cdm ON cdm.is_mgt_audit_id = ima.id " +
					"    </when>" +
					"    <when test='audit_type == \"assetLiability\"'>" +
					"        INNER JOIN asset_liability_mgt alm ON alm.is_mgt_audit_id = ima.id " +
					"    </when>" +
					"    <when test='audit_type == \"memorandomContingent\"'>" +
					"        INNER JOIN memorandom_contingent_mgt mcg ON mcg.is_mgt_audit_id = ima.id " +
					"    </when>" +
					"</choose>" +
					"WHERE isma.auditee_id = #{auditee_id} AND audit_type = #{audit_type} " +
					"ORDER BY adi.response_subimtted_date DESC" +
					"</script>"
	})

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "creditDocumentationParent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper.getCreditDocumentationParentByISMGTID")),
			@Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId"))
	})
	public List<AuditISM> getUnrectifiedAuditsForAuditee(@Param("auditee_id") Long auditee_id,
			@Param("audit_type") String audit_type);

	@Select("SELECT ima.*, "
			+ "adi.submitted_auditee AS division_response_added, "
			+ "adi.submitted_auditee AS divisionresponseadded, "
			+ "isma.division_assigned AS auditee_division_assigned, "
			+ "br.name AS division_name, "
			+ "adi.id AS division_auditee_id, isma.auditee_id as directorate_id, "
			+ "isma.id AS auditee_id, "
			+ "adi.response_subimtted_date AS division_response_date, "
			+ "isma.completed_date AS auditee_responded_date, "
			+ "isma.rectification_status AS auditee_rectification_status, "
			+ "isma.unrectification_date AS auditee_unrectification_date, "
			+ "isma.rectification_date AS auditee_rectification_date "
			+ "FROM IS_Management_Audit ima "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
			+ "AND isma.complete_status = 1 "
			+ "AND isma.rectification_status != 1 "
			+ "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
			+ "AND adi.auditee_submitted = 1 "
			+ "INNER JOIN branch br ON br.id = adi.division_id "
			+ "WHERE isma.auditee_id = #{auditee_id} "
			+ "ORDER BY isma.completed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getRespondedAuditsForAuditee(Long auditee_id);

	@Select("select * from IS_MGT_Auditee where IS_MGT_id = #{IS_MGT_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditee_id", column = "auditee_id"),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "auditeeDivisionISM", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAuditeeResponse")),
	})
	public List<IS_MGT_Auditee> getISMAuditees(Long IS_MGT_id);

	@Select("select * from IS_MGT_Auditee where IS_MGT_id = #{IS_MGT_id} and id in (Select IS_MGT_Auditee_id from Audtiee_Division_ISM where division_id = #{division_id})")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditee_id", column = "auditee_id"),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
	// @Result(property = "auditeeDivisionISM", column = "id", many = @Many(select =
	// "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAuditeeResponse")),
	})
	public List<IS_MGT_Auditee> getISMAuditeeByAuditandDivisionID(Long IS_MGT_id, Long division_id);

	@Select("select * from IS_MGT_Auditee where auditee_id = #{auditee_id} and IS_MGT_id = #{IS_MGT_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditee_id", column = "auditee_id"),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "auditeeDivisionISM", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAuditeeResponse")),
	})
	public List<IS_MGT_Auditee> getISMAuditeesByAuditeeID(Long auditee_id, Long IS_MGT_id);

	@Select("select * from IS_MGT_Auditee where id = #{auditee_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditee_id", column = "auditee_id"),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
	})
	public List<IS_MGT_Auditee> getISMAuditeesByID(Long auditee_id);

	@Select("select * from IS_MGT_Auditee where id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditISM", column = "IS_MGT_id", one = @One(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAudit")),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "auditeeDivisionISM", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAuditeeResponse")),
	})
	public IS_MGT_Auditee getISMAuditeeById(Long id);

	@Update("update IS_MGT_Auditee set division_assigned = 1 where id=#{audit_id}")
	public void updateDivisionAssignmentAuditee(Long audit_id);

	// while division uploading its response
	@Update("update Audtiee_Division_ISM set response_submitted = 1, submitted_auditee = 0, response_subimtted_date = CURRENT_TIMESTAMP where id = #{division_id};"
			+
			" update IS_MGT_Auditee set complete_status = 0 where id in (select IS_MGT_Auditee_id from Audtiee_Division_ISM where id = #{division_id});"
			+
			" update IS_Management_Audit set response_added = 0 where id in (select IS_MGT_id from IS_MGT_Auditee where id in (select IS_MGT_Auditee_id from Audtiee_Division_ISM where id = #{division_id}))")
	public void submitResponseStatus(Long division_id);

}
