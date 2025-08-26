package com.afr.fms.Approver.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;

@Mapper
public interface AuditISMApproverMapper {
	@Select("select * from IS_Management_Audit where category = #{category} and review_status = 1 and approve_status = 0 Order By reviewed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getAuditsForApprover(String category);

	@Update("update IS_Management_Audit set approver_id = #{approver.id}, approve_status=1, approved_date = CURRENT_TIMESTAMP,finding_status='approved' where id = #{id}")
	public void approveAudit(AuditISM audit);

	@Select("<script>" +
			"select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, isma.auditee_rejected as rejection_status, br.name as auditee_name from IS_Management_Audit  ism "
			+ //
			"inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.division_assigned != 1 and isma.self_response != 1"
			+ //
			"inner join Branch br on br.id = isma.auditee_id " +
			"where ism.approver_id= #{auditISM.approver.id} and ism.approve_status = 1 " +

			"  <if test=\"auditISM.finding != null\"> " +
			"    AND dbo._StripHTML(ism.finding) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.finding}), '%') "
			+
			"  </if> " +

			"  <if test=\"auditISM.impact != null\"> " +
			"    AND dbo._StripHTML(ism.impact) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.impact}), '%') "
			+
			"  </if> " +

			"  <if test=\"auditISM.finding_detail != null\"> " +
			"    AND dbo._StripHTML(ism.finding_detail) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.finding_detail}), '%') "
			+
			"  </if> " +

			"  <if test=\"auditISM.recommendation != null\"> " +
			"    AND dbo._StripHTML(ism.recommendation) LIKE CONCAT('%', dbo._StripHTML(#{auditISM.recommendation}), '%') "
			+
			"  </if> " +

			"  <if test=\"auditISM.auditees != null and auditISM.auditees.size() > 0\"> " +
			"    AND isma.auditee_id IN " +
			"    <foreach item='auditee' collection='auditISM.auditees' open='(' separator=',' close=')'> "
			+
			"        #{auditee.id} " +
			"    </foreach> " +
			"  </if> " +

			"  <if test=\"auditISM.auditors != null and auditISM.auditors.size() > 0\"> " +
			"    AND ism.auditor_id IN " +
			"    <foreach item='auditor' collection='auditISM.auditors' open='(' separator=',' close=')'> "
			+
			"        #{auditor.id} " +
			"    </foreach> " +
			"  </if> " +

			"  <if test=\"auditISM.finding_status != null\"> " +
			"    AND  (ism.finding_status LIKE CONCAT('%', #{auditISM.finding_status}, '%') or isma.finding_status LIKE CONCAT('%', #{auditISM.finding_status}, '%')) "
			+
			"  </if> " +

			"  <if test=\"auditISM.case_number != null\"> " +
			"    AND ism.case_number LIKE CONCAT('%', #{auditISM.case_number}, '%') " +
			"  </if> " +

			"  <if test=\"auditISM.risk_level != null\"> " +
			"    AND ism.risk_level = #{auditISM.risk_level} " +
			"  </if> " +

			"  <choose> " +
			"    <when test=\"start_requisition_date != null and end_requisition_date != null\"> " +
			"      AND (ism.finding_date BETWEEN #{start_requisition_date} AND #{end_requisition_date}) " +
			"    </when> " +
			"    <when test=\"start_requisition_date != null and end_requisition_date == null\"> " +
			"      AND ism.finding_date >= #{start_requisition_date} " +
			"    </when> " +
			"    <when test=\"start_requisition_date == null and end_requisition_date != null\"> " +
			"      AND #{end_requisition_date} >= ism.finding_date " +
			"    </when> " +
			"  </choose> " +
			" order by ism.approved_date DESC" +
			"</script>"

	)

	// @Select("select * from IS_Management_Audit where approver_id=#{approver_id}
	// and approve_status=1 and division_assigned != 1 order by approved_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getApprovedFindings(AuditISM auditISM,
			@Param("start_requisition_date") String start_requisition_date,
			@Param("end_requisition_date") String end_requisition_date);

	@Update("update IS_Management_Audit set approver_id = #{approver.id}, approve_status=0 , finding_status= 'unapproved' where id = #{id}")
	public void cancelApprovalISM_Audit(AuditISM audit);

	@Select("select * from IS_Management_Audit where approve_status = 2 and approver_id=#{approver_id} and status = 1 Order By approver_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getRejectedFindings(Long approver_id);

	@Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, isma.rectification_status as auditee_rectification_status, isma.division_assigned as auditee_division_assigned, isma.complete_status as auditee_responded_added, "
			+ " isma.rectification_date as auditee_rectification_date, isma.unrectification_date as auditee_unrectification_date, br.name as auditee_name, "
			+ " isma.completed_date as auditee_responded_date from IS_Management_Audit ism"
			+ " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.division_assigned = 1"
			+ " inner join Branch br on br.id = isma.auditee_id"
			+ " where ism.approver_id=#{approver_id} and ism.status = 1 and ism.approve_status = 1 Order By approved_date DESC ")

	// @Select("select * from IS_Management_Audit where approve_status = 1 and
	// division_assigned = 1 and approver_id=#{approver_id} and status = 1 Order By
	// approved_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			// @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select =
			// "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getApprovedFindingsStatus(Long approver_id);

	

	@Update("update IS_Management_Audit set approver_id=#{approver.id}, approve_status = 2, approver_rejected_date = CURRENT_TIMESTAMP, finding_status= 'approver_rejected' where id=#{id}")
	public void rejectFinding(AuditISM audit);

	@Update("update IS_MGT_Auditee set approver_id = #{approver_id}, approval_status = #{approval_status} , finding_status = #{finding_status}, approved_date = CURRENT_TIMESTAMP where id = #{id}")
	public void approveRectifiedFindings(IS_MGT_Auditee is_MGT_Auditee);

}
