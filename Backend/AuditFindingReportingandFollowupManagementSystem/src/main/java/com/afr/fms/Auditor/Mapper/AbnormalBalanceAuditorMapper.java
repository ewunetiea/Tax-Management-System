package com.afr.fms.Auditor.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Auditor.Entity.AbnormalBalance;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@Mapper
public interface AbnormalBalanceAuditorMapper {

	
    @Update("update IS_Management_Audit set management = #{management}, finding=#{finding}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = CAST(#{finding_date} AS DATE), is_edited = 1 where id=#{id}")
	public void updateMGTFinding(AuditISM audit);

	@Update("update IS_Management_Audit set management = #{management}, finding=#{finding}, finding_detail = #{finding_detail}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = CAST(#{finding_date} AS DATE), is_edited = 1 where id=#{id}")
	public void updateMGTFindingandFindingDetail(AuditISM audit);

    @Insert("insert into abnormal_balance_mgt (is_management_audit_id, abnormal_balance, account_number, account_type, abnormal_type)  values (#{is_management_audit_id},  #{abnormal_balance}, #{account_number}, #{account_type}, #{abnormal_type})")
	public void createAbnormalBalance(AbnormalBalance abnormalBalance);

	@Update(" UPDATE abnormal_balance_mgt SET is_management_audit_id = #{is_management_audit_id},  abnormal_balance = #{abnormal_balance},  account_number = #{account_number}, account_type = #{account_type}, abnormal_type = #{abnormal_type}  WHERE id = #{id}")
	public void updateAbnormalBalance(AbnormalBalance abnormalBalance);


	@Select("select  * from abnormal_balance_mgt where is_management_audit_id = #{is_management_audit_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public AbnormalBalance getAbnormalBalanceByManagementId(Long is_management_audit_id);

	@Select("select ima.* from IS_Management_Audit ima " + 
				"INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id " + 
				"where ima.auditor_id = #{user_id} and ima.auditor_status != 1 and ima.status = 1 Order By ima.drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
    public List<AuditISM> getAuditsOnDrafting(PaginatorPayLoad paginatorPayload);

	@Select("select ima.* from IS_Management_Audit ima " + 
				"INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id " + 
				"where ima.auditor_id = #{user_id} and ima.auditor_status = 1 and ima.status = 1 and ima.review_status = 0 Order By ima.passed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
    public List<AuditISM> getAuditsOnProgress(PaginatorPayLoad paginatorPayload);


	@Select("select ima.* from IS_Management_Audit ima " + 
				"INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id " + 
				"where ima.auditor_id = #{user_id} and ima.auditor_status = 1 and ima.status = 1 and ima.review_status = 2 Order By ima.reviewer_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
    public List<AuditISM> getRejectedFindings(PaginatorPayLoad paginatorPayload);


}
