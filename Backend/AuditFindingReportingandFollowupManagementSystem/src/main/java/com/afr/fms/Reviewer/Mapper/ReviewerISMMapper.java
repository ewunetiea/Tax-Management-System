package com.afr.fms.Reviewer.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.AuditISM;

@Mapper
public interface ReviewerISMMapper {

	@Select("select * from IS_Management_Audit where category = #{category}  and review_status = 0 and auditor_status = 1 and status = 1  Order By passed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getAuditsForReviewerIS(String category);

	@Select("select * from IS_Management_Audit where category = #{category} and management = #{banking} and review_status = 0 and auditor_status = 1 and status = 1  Order By passed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getAuditsForReviewerMGT(User user);

	@Update("update IS_Management_Audit set  reviewer_id=#{reviewer.id}, review_status = 1 , reviewed_date= CURRENT_TIMESTAMP, finding_status= 'reviewed' where id=#{id}")
	public void reviewFindings(AuditISM audit);

	@Update("update IS_Management_Audit set  reviewer_id=#{reviewer.id}, review_status = 0, approve_status = 0 , finding_status= 'Unreviewed'  where id=#{id}")
	public void unReviewISM_Audit_Findings(AuditISM audit);

	@Select("select * from IS_Management_Audit where review_status = 1 and reviewer_id=#{reviewer_id} and status = 1 and approve_status = 0  Order By reviewed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getReviewedFindings(Long reviewer_id);

	@Select("select * from IS_Management_Audit where review_status = 1 and reviewer_id=#{reviewer_id} and status = 1 and approve_status = 2  Order By approver_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getApproverRejectedFindings(Long reviewer_id);

	@Select("select * from IS_Management_Audit where review_status = 2 and reviewer_id=#{reviewer_id} and status = 1  Order By reviewer_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getRejectedFindings(Long reviewer_id);

	// @Select("select * from IS_Management_Audit where review_status = 1 and
	// reviewer_id=#{reviewer_id} and status = 1 and approve_status = 1 Order By
	// approved_date DESC")
	@Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, isma.rectification_status as auditee_rectification_status, isma.division_assigned as auditee_division_assigned, isma.complete_status as auditee_responded_added, "
			+ " isma.rectification_date as auditee_rectification_date, isma.unrectification_date as auditee_unrectification_date, br.name as auditee_name, "
			+ " isma.completed_date as auditee_responded_date from IS_Management_Audit ism"
			+ " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id"
			+ " inner join Branch br on br.id = isma.auditee_id"
			+ " where ism.reviewer_id=#{reviewer_id} and ism.status = 1 and ism.review_status = 1 and ism.approve_status = 1 Order By approved_date DESC ")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getReviewedFindingsStatus(Long reviewer_id);

	@Update("update IS_Management_Audit set reviewer_id=#{reviewer.id}, review_status = 2, reviewer_rejected_date = CURRENT_TIMESTAMP, finding_status= 'reviewer_rejected' where id=#{id}")
	public void cancelFinding(AuditISM audit);

}
