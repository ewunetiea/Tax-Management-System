package com.afr.fms.Followup_officer.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;

@Mapper
public interface FollowupOfficerISMMapper {

	// Full Rectification Status
	@Update("update IS_Management_Audit set rectification_status = 1 , finding_status = 'Fully Rectified', followup_id= #{followup_officer.id}, rectification_date = CURRENT_TIMESTAMP where id = #{id}")
	public void rectifyISMFindings(AuditISM auditISM);

	@Update("update IS_MGT_Auditee set rectification_status = 1, complete_status = 1, finding_status = 'Fully Rectified', rectification_date = CURRENT_TIMESTAMP where id = #{id}")
	public void rectifyFindingsAuditee(IS_MGT_Auditee is_MGT_Auditee);

	// UnRectification Status

	// It rarely used
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

	@Update("<script> " +
			"update Audtiee_Division_ISM set auditee_submitted = 0 " +
			"  <if test=\"self_response != 1\"> " +
			"   , submitted_auditee = 0 " +
			"  </if> " +
			"  where id=#{auditeeDivisionISM.id} " +
			" </script>")
	public void updateAuditeeDivision(int self_response, AuditeeDivisionISM auditeeDivisionISM);

	@Select("<script> select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, br.name as auditee_name, isma.completed_date as auditee_responded_date, isma.complete_status as auditee_responded_added, "
			+ " isma.rectification_date as auditee_rectification_date, isma.finding_status as auditee_finding_status from IS_Management_Audit ism"
			+ " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.rectification_status = 1 "
			+ " inner join Branch br on br.id = isma.auditee_id"
			+ " where ism.status = 1 " +

			"  <choose> " +
			"    <when test=\"auditISM.followup_officer != null\"> " +
			"   	AND ism.followup_id = #{auditISM.followup_officer.id} " +
			"    </when> " +
			"    <when test=\"auditISM.approver != null\"> " +
			"   	AND ism.category = #{auditISM.approver.category} " +
			"    </when> " +
			"  </choose> " +

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

			"Order By isma.rectification_date DESC </script>")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "followup_officer", column = "followup_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getRectifiedFindings(AuditISM auditISM,
			@Param("start_requisition_date") String start_requisition_date,
			@Param("end_requisition_date") String end_requisition_date);

	@Select("<script> select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, br.name as auditee_name, isma.completed_date as auditee_responded_date, isma.complete_status as auditee_responded_added, "
			+ " isma.rectification_date as auditee_rectification_date, isma.finding_status as auditee_finding_status, "
			+
			" CASE WHEN isma.approval_status = 1 THEN 'Rectification Approved' " +
			" WHEN isma.approval_status = 2 THEN 'Rectification Rejected' " +
			" ELSE 'Pending' END as rectification_approval_status,  " +

			" isma.approved_date as rectification_approval_date from IS_Management_Audit ism"
			+ " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.rectification_status = 1 "
			+ " inner join Branch br on br.id = isma.auditee_id"
			+ " where ism.status = 1 " +

			"  <choose> " +
			"    <when test=\"auditISM.followup_officer != null\"> " +
			"   	AND ism.followup_id = #{auditISM.followup_officer.id} " +
			"    </when> " +
			"    <when test=\"auditISM.approver != null\"> " +
			"   	AND ism.category = #{auditISM.approver.category} " +
			"    </when> " +
			"  </choose> " +

			"  <choose> " +
			"    <when test=\"auditISM.rectification_approval_status != null and auditISM.rectification_approval_status == 'pending' \"> "
			+
			"   	AND (isma.approval_status = 0 or isma.approval_status is null) " +
			"    </when> " +
			"    <when test=\"auditISM.rectification_approval_status != null and auditISM.rectification_approval_status == 'approved' \"> "
			+
			"   	AND isma.approval_status = 1  " +
			"    </when> " +
			"  </choose> " +

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

			"Order By isma.rectification_date DESC </script>")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "followup_officer", column = "followup_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getRectifiedISFindings(AuditISM auditISM,
			@Param("start_requisition_date") String start_requisition_date,
			@Param("end_requisition_date") String end_requisition_date);

	@Select("<script> select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, br.name as auditee_name, isma.division_assigned as auditee_division_assigned, "
			+ " isma.complete_status as auditee_responded_added, "
			+ " isma.unrectification_date as auditee_unrectification_date, isma.completed_date as auditee_responded_date from IS_Management_Audit ism"
			+ " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.rectification_status = 2 "
			+ " inner join Branch br on br.id = isma.auditee_id"
			+ " where ism.followup_id = #{auditISM.followup_officer.id} and ism.status = 1 " +

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

			"Order By isma.unrectification_date DESC </script>")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),

			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getUnrectifiedFindings(AuditISM auditISM,
			@Param("start_requisition_date") String start_requisition_date,
			@Param("end_requisition_date") String end_requisition_date);

	@Select("<script> select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, br.name as auditee_name, isma.rectification_date as auditee_rectification_date, isma.completed_date as auditee_responded_date, "
			+ " isma.rectification_status as auditee_rectification_status, isma.division_assigned as auditee_division_assigned, isma.complete_status as auditee_responded_added from IS_Management_Audit ism"
			+ " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.rectification_status = 4 "
			+ " inner join Branch br on br.id = isma.auditee_id"
			+ " where ism.followup_id = #{auditISM.followup_officer.id} and ism.status = 1 " +
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
			"Order By isma.rectification_date DESC </script>")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),

			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getPartiallyRectifiedFindings(AuditISM auditISM,
			@Param("start_requisition_date") String start_requisition_date,
			@Param("end_requisition_date") String end_requisition_date);

	@Select(" select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, br.name as auditee_name, isma.division_assigned as auditee_division_assigned, "
			+ " isma.complete_status as auditee_responded_added, "
			+ " isma.unrectification_date as auditee_unrectification_date, isma.completed_date as auditee_responded_date from IS_Management_Audit ism"
			+ " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.rectification_status = 2 "
			+ " inner join Branch br on br.id = isma.auditee_id"
			+ " where ism.followup_id = #{followup_officer_id} and ism.status = 1 Order By isma.unrectification_date DESC ")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),

			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getRejectedFindings(Long followup_officer_id);

	@Select("<script> select ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, br.name as auditee_name, isma.completed_date as auditee_responded_date, isma.complete_status as auditee_responded_added from IS_Management_Audit ism"
			+ " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.complete_status = 1 and isma.rectification_status = 0 "
			+ " inner join Branch br on br.id = isma.auditee_id"
			+ " where ism.category = #{auditISM.followup_officer.category} and ism.auditor_id = #{auditISM.followup_officer.id} and ism.status = 1 "
			+
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
			"Order By isma.completed_date DESC </script> ")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getAuditsForFollowupOfficerMGT(AuditISM auditISM,
			@Param("start_requisition_date") String start_requisition_date,
			@Param("end_requisition_date") String end_requisition_date);

	@Select("<script> "
			+ "SELECT ism.*, isma.id as auditee_id, isma.auditee_id as directorate_id, br.name as auditee_name, "
			+ "isma.completed_date as auditee_responded_date, isma.complete_status as auditee_responded_added "
			+ "FROM IS_Management_Audit ism "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id AND isma.complete_status = 1 AND isma.rectification_status = 0 "
			+ "INNER JOIN Branch br ON br.id = isma.auditee_id "
			+ "where ism.category = #{auditISM.followup_officer.category} AND ism.status = 1 " +
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
			"  </choose> "
			+ "ORDER BY isma.completed_date DESC "
			+ "</script>")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getAuditsForFollowupOfficerIS(AuditISM auditISM,
			@Param("start_requisition_date") String start_requisition_date,
			@Param("end_requisition_date") String end_requisition_date);

}
