package com.afr.fms.Auditee.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
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
public interface AuditeeDivisionISMMapper {

	@Select("update Audtiee_Division_ISM set action_plan = #{action_plan}, scheduled_date = #{scheduled_date}, responder_id = #{responder_id} where id = #{id}")
	public Long add_response(AuditeeDivisionISM auditeeDivisionISM);

	@Select("insert into Audtiee_Division_ISM (IS_MGT_Auditee_id, action_plan, scheduled_date, response_submitted, response_subimtted_date, submitted_auditee, responder_id) OUTPUT inserted.id values (#{IS_MGT_Auditee_id}, #{action_plan}, #{scheduled_date}, 1, CURRENT_TIMESTAMP, 1, #{responder_id});"
			+ " update IS_MGT_Auditee set self_response = 1 where id = #{IS_MGT_Auditee_id};")
	public Long add_self_response(AuditeeDivisionISM auditeeDivisionISM);

	@Select("update Audtiee_Division_ISM set action_plan = #{action_plan}, scheduled_date = #{scheduled_date}, responder_id = #{responder_id} where id = #{id}")
	public Long update_response(AuditeeDivisionISM auditeeDivisionISM);

	@Update("update Audtiee_Division_ISM set  previous_action_plan = #{action_plan} where id = #{id}")
	public Long update_previous_response(AuditeeDivisionISM auditeeDivisionISM);

	@Update("update Audtiee_Division_ISM set action_plan=#{action_plan}, scheduled_date = #{scheduled_date}, responder_id = #{responder_id} where id=#{id}")
	public void updateAuditeeResponse(AuditeeDivisionISM auditeeDivisionISM);

	@Select("insert into Auditee_Division_File_ISM(auditee_division_id, file_url) values(#{auditee_division_id}, #{file_url})")
	public Long add_attached_files(Long auditee_division_id, String file_url);

	@Select("insert into Auditee_Division_File_ISM(auditee_division_id, previous_file_url) values(#{auditee_division_id}, #{file_url})")
	public Long add_previously_attached_files(Long auditee_division_id, String file_url);

	@Delete("delete from Auditee_Division_File_ISM where auditee_division_id = #{auditee_division_id} and file_url is not null ")
	public void deleteAttachedFiles(Long auditee_division_id);

	@Delete("delete from Auditee_Division_File_ISM where auditee_division_id = #{auditee_division_id} and previous_file_url is not null ")
	public void deletePreviouslyAttachedFiles(Long auditee_division_id);

	// @Select("select * from Audtiee_Division_ISM where IS_MGT_Auditee_id =
	// #{IS_MGT_Auditee_id} and submitted_auditee = 1")
	@Select("select * from Audtiee_Division_ISM where IS_MGT_Auditee_id = #{IS_MGT_Auditee_id} ")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAttachedFiles")),
			@Result(property = "previously_uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getPreviouslyAttachedFiles"))
	})
	public List<AuditeeDivisionISM> getAuditeeResponse(Long IS_MGT_Auditee_id);

	@Select("select * from Audtiee_Division_ISM where IS_MGT_Auditee_id = #{IS_MGT_Auditee_id} and scheduled_date is not null ")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAttachedFiles")),
			@Result(property = "previously_uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getPreviouslyAttachedFiles"))
	})
	public List<AuditeeDivisionISM> getScheduledAuditeeResponse(Long IS_MGT_Auditee_id);

	@Select("select * from IS_Management_Audit where id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),

	})
	public AuditISM getCommonAuditByID(Long id);

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
			@Result(property = "previously_uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getPreviouslyAttachedFiles")),
			@Result(property = "uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAttachedFiles"))
	})
	public List<AuditeeDivisionISM> getAuditeeResponseByAuditeeandDivisionID(Long division_id, Long IS_MGT_Auditee_id);

	@Select("select * from Audtiee_Division_ISM where id = #{auditee_division_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "previously_uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getPreviouslyAttachedFiles")),
			@Result(property = "uploaded_files", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAttachedFiles"))
	})
	public AuditeeDivisionISM getAuditeeResponseISM(Long auditee_division_id);

	@Select("select * from Auditee_Division_File_ISM where auditee_division_id = #{auditee_division_id} and file_url is not null ")

	public List<AuditeeDivisionFileISM> getAttachedFiles(Long auditee_division_id);

	@Select("select * from Auditee_Division_File_ISM where auditee_division_id = #{auditee_division_id} and previous_file_url is not null ")

	public List<AuditeeDivisionFileISM> getPreviouslyAttachedFiles(Long auditee_division_id);

	@Update("update Audtiee_Division_ISM set submitted_auditee = 1  where id=#{auditeeDivision_id}")
	public void submitResponsetoAuditee(Long auditeeDivision_id);

	@Select("select * from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where id = #{auditee_id})")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public AuditISM getAuditByAuditeeID(Long auditee_id);

	@Update("update IS_Management_Audit set auditee_submitted = 1 where id = #{audit_id}")
	public void submitAuditAuditeeResponse(Long audit_id);

	@Select("select * from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and complete_status = 1) and response_added = 1 Order By responded_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			// @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select =
			// "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getRespondedAuditsForAuditee(Long auditee_id);

	@Select("SELECT ima.*, "
			+ "br.name AS division_name, "
			+ "adi.id AS division_auditee_id, isma.id as auditee_id, isma.auditee_id as directorate_id  "
			+ "FROM IS_Management_Audit ima "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id and isma.division_assigned = 1 "
			+ "AND isma.rectification_status = 0 "
			+ "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  and adi.response_submitted != 1 "
			+ "INNER JOIN branch br ON br.id = adi.division_id "
			+ "WHERE adi.division_id = #{division_id} ORDER BY ima.approved_date")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getAuditsForAuditeeDivision(Long division_id);

	@Select("SELECT ima.*, "
			+ "adi.submitted_auditee AS division_response_added, "
			+ "adi.submitted_auditee AS divisionresponseadded, "
			+ "isma.division_assigned AS auditee_division_assigned, "
			+ "br.name AS division_name, "
			+ "adi.id AS division_auditee_id, isma.id as auditee_id, isma.auditee_id as directorate_id, "
			+ "adi.response_subimtted_date AS division_response_date, "
			+ "isma.rectification_status AS auditee_rectification_status, "
			+ "isma.rectification_date AS auditee_rectification_date "
			+ "FROM IS_Management_Audit ima "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
			+ "AND isma.rectification_status = 4 "
			+ "AND isma.complete_status != 1 "
			+ "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
			// + " AND adi.auditee_submitted != 1 "
			+ "INNER JOIN branch br ON br.id = adi.division_id "
			+ "WHERE adi.division_id = #{division_id} "
			+ "ORDER BY isma.rectification_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getPartiallyRectifiedAuditsForAuditeeDivision(Long division_id);

	@Select("SELECT ima.*, "
			+ "adi.submitted_auditee AS division_response_added, "
			+ "adi.submitted_auditee AS divisionresponseadded, "
			+ "isma.division_assigned AS auditee_division_assigned, "
			+ "br.name AS division_name, "
			+ "adi.id AS division_auditee_id, isma.id as auditee_id, isma.auditee_id as directorate_id, "
			+ "adi.response_subimtted_date AS division_response_date "
			+ "FROM IS_Management_Audit ima "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
			+ "AND isma.rectification_status = 0 "
			+ "AND isma.complete_status != 1 "
			+ "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
			+ "AND adi.response_submitted = 1 "
			// + "AND adi.auditee_submitted != 1 "
			+ "INNER JOIN branch br ON br.id = adi.division_id "
			+ "WHERE adi.division_id = #{division_id} "
			+ "ORDER BY adi.response_subimtted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getAuditsForAuditeeDivisionOnProgress(Long division_id);

	@Select("SELECT ima.*, "
			+ "adi.submitted_auditee AS division_response_added, "
			+ "adi.submitted_auditee AS divisionresponseadded, "
			+ "isma.division_assigned AS auditee_division_assigned, "
			+ "br.name AS division_name, "
			+ "adi.id AS division_auditee_id, isma.id as auditee_id, isma.auditee_id as directorate_id, "
			+ "adi.response_subimtted_date AS division_response_date, "
			+ "isma.rectification_status AS auditee_rectification_status, "
			+ "isma.unrectification_date AS auditee_unrectification_date "
			+ "FROM IS_Management_Audit ima "
			+ "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
			+ "AND isma.rectification_status = 2 "
			+ "AND isma.complete_status != 1 "
			+ "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
			// + "AND adi.auditee_submitted != 1 "
			+ "INNER JOIN branch br ON br.id = adi.division_id "
			+ "WHERE adi.division_id = #{division_id} "
			+ "ORDER BY isma.unrectification_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getUnrectifiedAuditsForAuditeeDivision(Long division_id);

	@Select("SELECT ima.*, "
			+ "adi.submitted_auditee AS division_response_added, "
			+ "isma.division_assigned AS auditee_division_assigned, "
			+ "br.name AS division_name, "
			+ "adi.id AS division_auditee_id, isma.id as auditee_id, isma.auditee_id as directorate_id, "
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
			+ "WHERE adi.division_id = #{division_id} "
			+ "ORDER BY isma.completed_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
	})
	public List<AuditISM> getRespondedAuditsForAuditeeDivision(Long division_id);

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

	@Select("select * from IS_MGT_Auditee where auditee_id = #{auditee_id} and IS_MGT_id = #{IS_MGT_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditee_id", column = "auditee_id"),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "auditeeDivisionISM", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getScheduledAuditeeResponse")),
	})
	public List<IS_MGT_Auditee> getScheduledISMAuditeesByAuditeeID(Long auditee_id, Long IS_MGT_id);

	@Select("select * from IS_MGT_Auditee where id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditISM", column = "IS_MGT_id", one = @One(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAudit")),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "auditeeDivisionISM", column = "id", many = @Many(select = "com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper.getAuditeeResponse")),
	})
	public IS_MGT_Auditee getISMAuditeeById(Long id);

	// while division uploading its response
	@Update("update Audtiee_Division_ISM set response_submitted = 1, submitted_auditee = 0, auditee_submitted = 0, response_subimtted_date = CURRENT_TIMESTAMP where id = #{division_id};"
			+
			" update IS_MGT_Auditee set complete_status = 0 where id in (select IS_MGT_Auditee_id from Audtiee_Division_ISM where id = #{division_id});"
			+
			" update IS_Management_Audit set response_added = 0 where id in (select IS_MGT_id from IS_MGT_Auditee where id in (select IS_MGT_Auditee_id from Audtiee_Division_ISM where id = #{division_id}))")
	public void submitResponseStatus(Long division_id);

	@Delete({
			"<script>",
			"DELETE FROM Audtiee_Division_ISM",
			"WHERE action_plan IS NULL",
			"AND id IN",
			"<foreach item='id' collection='idList' open='(' separator=',' close=')'>",
			"#{id}",
			"</foreach>",
			"</script>"
	})
	public void clearDivisionMappingDuplicateFindings(@Param("idList") List<Long> idList);

	@Select("SELECT COUNT(*) FROM Audtiee_Division_ISM WHERE division_id != #{division_id} AND IS_MGT_Auditee_id = #{IS_MGT_Auditee_id}")
	public int countAuditeeOtherDivision(@Param("division_id") Long division_id,
			@Param("IS_MGT_Auditee_id") Long IS_MGT_Auditee_id);

	@Delete("DELETE FROM IS_MGT_Auditee WHERE id = #{id}")
	public void clearAuditeeMappingDuplicateFindings(@Param("id") Long id);

}
