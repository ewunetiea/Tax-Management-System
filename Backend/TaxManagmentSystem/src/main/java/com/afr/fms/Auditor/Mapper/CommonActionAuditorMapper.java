package com.afr.fms.Auditor.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Auditor.Entity.AuditISM;

@Mapper
public interface CommonActionAuditorMapper {
    
	@Update("update IS_Management_Audit set finding=#{finding}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, risk_level=#{risk_level}, finding_date = #{finding_date}, is_edited = 1 where id=#{id}")
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
  
}
