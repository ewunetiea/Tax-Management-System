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
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Model.MGT.SuspenseAccount;

@Mapper
public interface SuspenseAccountAuditorMapper {

    	@Select("select * from IS_Management_Audit where id in (select is_mgt_audit_id  from suspense_account_mgt) AND auditor_id = #{user_id} and auditor_status != 1 and status = 1 Order By drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
		    @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getDraftedSuspenseAccount(PaginatorPayLoad paginatorPayLoad);


    @Select("select * from IS_Management_Audit where  id in (select is_mgt_audit_id  from suspense_account_mgt) AND auditor_id = #{user_id} and auditor_status = 1  AND review_status = 0 and status = 1 Order By drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getPassedSuspenseAccount(PaginatorPayLoad paginatorPayLoad);

	
	@Select("select * from IS_Management_Audit where id in (SELECT is_mgt_audit_id from  suspense_account_mgt)  AND auditor_id = #{user_id} and auditor_status = 1  and review_status = 2 and status = 1 Order By reviewer_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "suspenseAccount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper.getSespenseAccoutByISMGTAuditId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getRejectedFindings(PaginatorPayLoad paginatorPayLoad);


    @Select("Select *  from suspense_account_mgt where is_mgt_audit_id = #{is_mgt_audit_id}")
    public SuspenseAccount  getSespenseAccoutByISMGTAuditId(Long is_mgt_audit_id );



    @Insert("INSERT INTO suspense_account_mgt (is_mgt_audit_id, difference, tracer_date, balance_per_tracer, balance_per_trial_balance, cash_type, fcy, account_number , account_type) " +
            "VALUES (#{is_mgt_audit_id}, #{difference}, #{tracer_date}, #{balance_per_tracer}, #{balance_per_trial_balance}, #{cash_type}, #{fcy}, #{account_number} , #{account_type})")
    public void  createSuspenseAccount(SuspenseAccount suspenseAccount);

	@Update("UPDATE suspense_account_mgt " +
	"SET difference = #{difference}, " +
	"tracer_date = #{tracer_date}, " +
	"balance_per_tracer = #{balance_per_tracer}, " +
	"balance_per_trial_balance = #{balance_per_trial_balance}, " +
	"cash_type = #{cash_type}, " +
	"fcy = #{fcy}, " +
	"account_number = #{account_number}, " +
	"account_type = #{account_type} " +  // <-- Space at the end here is crucial
	"WHERE id = #{id}")
void updateSuspenseAccount(SuspenseAccount suspenseAccount);

    
    
}
