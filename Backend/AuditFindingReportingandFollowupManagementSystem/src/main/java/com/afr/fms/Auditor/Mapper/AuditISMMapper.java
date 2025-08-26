package com.afr.fms.Auditor.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
public interface AuditISMMapper {
	@Select("insert into IS_Management_Audit(case_number, target_division_name, auditor_id, finding_date, finding, amount, cash_type, fcy, impact, recommendation, risk_level, category, save_template, status, is_edited, auditor_status, drafted_date, management) OUTPUT inserted.id values(#{case_number}, #{target_division_name}, #{auditor.id}, #{finding_date}, #{finding}, #{amount}, #{cash_type}, #{fcy}, #{impact}, #{recommendation}, #{risk_level}, #{category}, #{save_template}, 1, 1, 0, CURRENT_TIMESTAMP, #{management})")
	public Long createMGTFinding(AuditISM audit);

	@Select("INSERT INTO IS_Management_Audit(" +
			"case_number, target_division_name, auditor_id, finding_date, finding, finding_detail, amount, cash_type, fcy, "
			+
			"impact, recommendation, risk_level, category, save_template, status, " +
			"is_edited, auditor_status, drafted_date, management, audit_type) " +
			"OUTPUT inserted.id " +
			"VALUES (" +
			"#{case_number}, #{target_division_name}, #{auditor.id}, CAST(#{finding_date} AS DATE), COALESCE(#{finding}, ''), COALESCE(#{finding_detail}, ''), #{amount}, #{cash_type}, #{fcy}, "
			+
			"COALESCE(#{impact}, ''), COALESCE(#{recommendation}, ''), #{risk_level}, #{category}, " +
			"#{save_template}, 1, 1, 0, CURRENT_TIMESTAMP, #{management}, #{audit_type})")
	Long createMGTFindingEnhanced(AuditISM audit);

	@Select("insert into IS_Management_Audit(case_number, target_division_name, auditor_id, finding_date, finding, amount, impact, recommendation, risk_level, category, save_template, status, is_edited, auditor_status, drafted_date, audit_type) OUTPUT inserted.id values(#{case_number}, #{target_division_name}, #{auditor.id}, #{finding_date}, #{finding}, #{amount}, #{impact}, #{recommendation}, #{risk_level}, #{category}, #{save_template}, 1, 1, 0, CURRENT_TIMESTAMP, #{audit_type})")
	public Long createISFinding(AuditISM audit);

	@Select("SELECT STRING_AGG(br.name, ', ') AS auditee_name " +
			"FROM IS_Management_Audit ism " +
			"INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id " +
			"INNER JOIN Branch br ON br.id = isma.auditee_id " +
			"WHERE ism.id = #{audit_id}")
	public String getAuditeeName(Long audit_id);

	@Select("select * from IS_Management_Audit where auditor_id = #{user_id} and auditor_status = 1  and review_status  = 0 and status = 1 and audit_type = #{audit_type} Order By passed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getPassedAuditsForAuditor(PaginatorPayLoad paginatorPayLoad);

	@Select("select * from IS_Management_Audit where auditor_id = #{auditor_id} and auditor_status = 1  and review_status = 2 and status = 1 Order By reviewer_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getRejectedAuditsForAuditor(Long auditor_id);

	@Select("select * from IS_Management_Audit where auditor_id = #{user_id} and auditor_status != 1 and status = 1 and audit_type = #{audit_type} Order By drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "auditee_name", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAuditeeName")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getAuditsOnDrafting(PaginatorPayLoad paginatorPayLoad);

	@Select("SELECT ism.*, " +
			"       isma.id AS auditee_id, " +
			"       isma.auditee_id AS directorate_id, " +
			"       isma.rectification_status AS auditee_rectification_status, " +
			"       isma.division_assigned AS auditee_division_assigned, " +
			"       isma.complete_status AS auditee_responded_added, " +
			"       isma.rectification_date AS auditee_rectification_date, " +
			"       isma.unrectification_date AS auditee_unrectification_date, " +
			"       br.name AS auditee_name, " +
			"       isma.completed_date AS auditee_responded_date " +
			"FROM IS_Management_Audit ism " +
			"INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id " +
			"INNER JOIN Branch br ON br.id = isma.auditee_id " +
			"WHERE ism.status = 1 " +
			"  AND ism.auditor_status = 1 " +
			"  AND ism.review_status = 1 " +
			"  AND EXISTS ( " +
			"      SELECT 1 " +
			"      FROM user_role ur1 " +
			"      JOIN user_role ur2 ON ur1.role_id = ur2.role_id " +
			"      WHERE ur1.user_id = ism.auditor_id " +
			"        AND ur2.user_id = #{auditor_id} " +
			"  ) " +
			"ORDER BY reviewed_date DESC")

	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getAuditsOnProgressForAuditor(Long auditor_id);

	@Select("select * from IS_Management_Audit where id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public AuditISM getAudit(Long id);

	@Select("SELECT TOP 1 case_number FROM IS_Management_Audit ORDER BY id DESC;")
	public String getLatestCaseNumber();

	@Insert("insert into IS_MGT_Auditee(IS_MGT_id, auditee_id) values(#{auditISM_id}, #{auditee_id})")
	public void createISMAuditee(IS_MGT_Auditee IS_MGTAuditee);

	@Select("select * from IS_MGT_Auditee where IS_MGT_id = #{IS_MGT_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditee_id", column = "auditee_id"),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "auditeeDivisionISM", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAuditeeResponse")),
	})
	public List<IS_MGT_Auditee> getISMAuditees(Long IS_MGT_id);

	@Update("update IS_Management_Audit set target_division_name = #{target_division_name}, management = #{management}, finding=#{finding}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = CAST(#{finding_date} AS DATE), is_edited = 1 where id=#{id}")
	public void updateMGTFinding(AuditISM audit);

	@Update("update IS_Management_Audit set target_division_name = #{target_division_name}, management = #{management}, finding=#{finding}, finding_detail = #{finding_detail}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = CAST(#{finding_date} AS DATE), is_edited = 1 where id=#{id}")
	public void updateMGTFindingandFindingDetail(AuditISM audit);

	@Update("update IS_Management_Audit set target_division_name = #{target_division_name}, finding=#{finding}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, risk_level=#{risk_level}, finding_date = #{finding_date}, is_edited = 1 where id=#{id}")
	public void updateISFinding(AuditISM audit);

	@Update("update IS_Management_Audit set status=0 where id=#{id}")
	public void deleteISMFinding(Long id);

	@Update("update IS_Management_Audit set auditor_status=1, passed_date = CURRENT_TIMESTAMP where id=#{id}")
	public void passISMFinding(Long id);

	@Update("update IS_Management_Audit set auditor_status = 0, review_status = 0 where id=#{id}")
	public void backISMFinding(Long id);

	@Delete("delete from IS_MGT_Auditee where IS_MGT_id = #{IS_MGT_id}")
	public void deleteISMAuditee(Long IS_MGT_id);

	@Update("update IS_Management_Audit set rectification_status = #{rectification_status} where id = #{id}")
	public void changeRecitificationStatus(AuditISM auditISM);

	@Update("update IS_Management_Audit set  reviewer_id=#{reviewer.id}, review_status = 1 where id=#{id}")
	public void reviewFindings(AuditISM audit);

	@Update("update IS_Management_Audit set  reviewer_id=#{reviewer.id}, review_status = 0 where id=#{id}")
	public void unReviewISM_Audit_Fundings(AuditISM audit);

	@Update("update IS_Management_Audit set rectification_status = 1 , followup_id= #{followup_officer.id}  where id = #{id}")
	public void rectifyISM_Audit_findig(AuditISM auditISM);

	@Update("update IS_Management_Audit set rectification_status = 0 , followup_id= #{followup_officer.id}  where id = #{id}")

	public void unRectifyISM_Audit_findig(AuditISM audit);

	@Update("update IS_Management_Audit set approver_id = #{approver.id}, approve_status=1 where id = #{id}")
	public void approveAudit(AuditISM audit);

	@Select("select * from IS_Management_Audit where approver_id=#{approver_id}  and approve_status=1 and status = 1")
	public List<AuditISM> getApproved_fundings(Long approver_id);

	@Update("update IS_Management_Audit set approver_id = #{approver.id}, approve_status=0 where id = #{id}")
	public void calcelApprovalISM_Audit(AuditISM audit);

	@Select("select * from IS_Management_Audit Order By finding_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getAuditFindings();

	@Select("select ism.* from IS_Management_Audit ism " +
			"INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ism.id " +
			"INNER JOIN Branch br ON br.id = isma.auditee_id " +
			"WHERE ism.finding LIKE '%' + #{finding} + '%' AND ism.status = 1")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	List<AuditISM> checkFindingsExist(String finding);

	@Select("select * from IS_Management_Audit where auditor_id = #{user_id}  and audit_type = #{audit_type} and auditor_status != 1 and status = 1  Order By drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getAuditsOnDraftingMGT(PaginatorPayLoad paginatorPayload);

}
