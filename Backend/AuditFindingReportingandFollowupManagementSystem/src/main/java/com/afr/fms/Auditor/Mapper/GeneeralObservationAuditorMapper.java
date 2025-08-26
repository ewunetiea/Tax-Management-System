package com.afr.fms.Auditor.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Many;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@Mapper
public interface GeneeralObservationAuditorMapper {
	// @Select("insert into IS_Management_Audit(case_number, auditor_id,
	// finding_date, finding, cash_type, fcy, impact, recommendation, risk_level,
	// category, save_template, status, is_edited, auditor_status, drafted_date,
	// management, audit_type) OUTPUT inserted.id values(#{case_number},
	// #{auditor.id}, #{finding_date}, #{finding}, #{cash_type}, #{fcy}, #{impact},
	// #{recommendation}, #{risk_level}, #{category}, #{save_template}, 1, 1, 0,
	// CURRENT_TIMESTAMP, #{management}, 'GeneralObservation')")
	// public Long createGeneralObservationFinding(AuditISM audit);

	@Select("INSERT INTO IS_Management_Audit(" +
			"case_number, auditor_id, finding_date, finding, cash_type, fcy, " +
			"impact, recommendation, risk_level, category, save_template, " +
			"status, is_edited, auditor_status, drafted_date, management, audit_type, finding_detail" +
			") " +
			"OUTPUT inserted.id " +
			"VALUES (" +
			"#{case_number}, #{auditor.id}, #{finding_date}, COALESCE(#{finding}, ''), " +
			"#{cash_type}, #{fcy}, COALESCE(#{impact}, ''), COALESCE(#{recommendation}, ''), " +
			"#{risk_level}, #{category}, #{save_template}, 1, 1, 0, CURRENT_TIMESTAMP, " +
			"#{management}, 'GeneralObservation', COALESCE(#{finding_detail}, '')" +
			")")
	public Long createGeneralObservationFinding(AuditISM audit);

	// @Select("insert into IS_Management_Audit(case_number, auditor_id,
	// finding_date, finding, finding_detail, cash_type, fcy, impact,
	// recommendation, risk_level, category, save_template, status, is_edited,
	// auditor_status, drafted_date, management, audit_type) OUTPUT inserted.id
	// values(#{case_number}, #{auditor.id}, #{finding_date}, #{finding},
	// #{finding_detail}, #{cash_type}, #{fcy}, #{impact}, #{recommendation},
	// #{risk_level}, #{category}, #{save_template}, 1, 1, 0, CURRENT_TIMESTAMP,
	// #{management}, 'GeneralObservation')")
	// public Long createGeneralObservationFindingDetail(AuditISM audit);

	@Update("update IS_Management_Audit set management = #{management}, finding=#{finding}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = #{finding_date}, is_edited = 1 where id=#{id}")
	public void updateGeneralObservationFinding(AuditISM audit);

	@Update("update IS_Management_Audit set management = #{management}, finding=#{finding}, finding_detail = #{finding_detail}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = #{finding_date}, is_edited = 1 where id=#{id}")
	public void updateGeneralObservationFindingandFindingDetail(AuditISM audit);

	@Select("select ima.* from IS_Management_Audit ima " +
			"where ima.auditor_id = #{user_id} and ima.auditor_status != 1 and ima.status = 1 and ima.audit_type = #{audit_type} Order By ima.drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getAuditsOnDrafting(PaginatorPayLoad paginatorPayload);

	@Select("select ima.* from IS_Management_Audit ima " +
			"where ima.auditor_id = #{user_id} and ima.auditor_status = 1 and ima.status = 1 and ima.review_status = 0 and ima.audit_type = #{audit_type} Order By ima.passed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getAuditsOnProgress(PaginatorPayLoad paginatorPayload);

	@Select("select ima.* from IS_Management_Audit ima " +
			"where ima.auditor_id = #{user_id} and ima.auditor_status = 1 and ima.status = 1 and ima.review_status = 2 and ima.audit_type = #{audit_type} Order By ima.reviewer_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getRejectedFindings(PaginatorPayLoad paginatorPayload);

}
