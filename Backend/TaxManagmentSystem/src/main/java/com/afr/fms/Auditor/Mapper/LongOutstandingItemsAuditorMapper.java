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
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.LongOutstandingItems;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@Mapper
public interface LongOutstandingItemsAuditorMapper {
    @Update("update IS_Management_Audit set management = #{management}, finding=#{finding}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = #{finding_date}, is_edited = 1 where id=#{id}")
	public void updateMGTFinding(AuditISM audit);

	@Update("update IS_Management_Audit set management = #{management}, finding=#{finding}, finding_detail = #{finding_detail}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = #{finding_date}, is_edited = 1 where id=#{id}")
	public void updateMGTFindingandFindingDetail(AuditISM audit);

    @Insert("insert into long_outstanding_item_mgt (mgt_audit_id, less_than_90_number, less_than_90_amount, justification, outstanding_item, greater_than_90_amount, greater_than_90_number, greater_than_180_amount, greater_than_180_number, greater_than_360_amount, greater_than_360_number, trial_balance, total_amount, difference, selected_item_age)  values (#{mgt_audit_id},  #{less_than_90_number}, #{less_than_90_amount}, #{justification}, #{outstanding_item}, #{greater_than_90_amount}, #{greater_than_90_number}, #{greater_than_180_amount}, #{greater_than_180_number}, #{greater_than_360_amount}, #{greater_than_360_number}, #{trial_balance}, #{total_amount}, #{difference}, #{selected_item_age})")
    public void createLongOutstandingItemsFinding(LongOutstandingItems longOutstandingItems);

    @Update(" UPDATE long_outstanding_item_mgt SET mgt_audit_id = #{mgt_audit_id},  less_than_90_number = #{less_than_90_number},  less_than_90_amount = #{less_than_90_amount}, justification = #{justification}, outstanding_item = #{outstanding_item}, greater_than_90_amount = #{greater_than_90_amount}, greater_than_90_number = #{greater_than_90_number}, greater_than_180_amount = #{greater_than_180_amount}, greater_than_180_number = #{greater_than_180_number}, greater_than_360_amount = #{greater_than_360_amount}, greater_than_360_number = #{greater_than_360_number}, trial_balance = #{trial_balance}, total_amount = #{total_amount}, difference = #{difference}, selected_item_age = #{selected_item_age}  WHERE id = #{id}")
	public void updateLongOutstandingItemsFinding(LongOutstandingItems longOutstandingItems);

    @Select("select * from long_outstanding_item_mgt where mgt_audit_id = #{management_audit_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public LongOutstandingItems getLongOutstandingItmesByManagementId(Long management_audit_id);

	@Select("select ima.* from IS_Management_Audit ima " + 
				"INNER JOIN long_outstanding_item_mgt lom on ima.id = lom.mgt_audit_id " + 
				"where ima.auditor_id = #{user_id} and ima.auditor_status != 1 and ima.status = 1 Order By ima.drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
    public List<AuditISM> getAuditsOnDrafting(PaginatorPayLoad paginatorPayload);

	@Select("select ima.* from IS_Management_Audit ima " + 
				"INNER JOIN long_outstanding_item_mgt lom on ima.id = lom.mgt_audit_id " + 
				"where ima.auditor_id = #{user_id} and ima.auditor_status = 1 and ima.status = 1 and ima.review_status = 0 Order By ima.passed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
    public List<AuditISM> getPassedAuditFindings(PaginatorPayLoad paginatorPayload);

	@Select("select ima.* from IS_Management_Audit ima " + 
				"INNER JOIN long_outstanding_item_mgt lom on ima.id = lom.mgt_audit_id " + 
				"where ima.auditor_id = #{user_id} and ima.auditor_status = 1 and ima.status = 1 and ima.review_status = 2 Order By ima.reviewer_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "longOutstandingItems", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.LongOutstandingItemsAuditorMapper.getLongOutstandingItmesByManagementId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
    public List<AuditISM> getRejectedFindings(PaginatorPayLoad paginatorPayload);

}
