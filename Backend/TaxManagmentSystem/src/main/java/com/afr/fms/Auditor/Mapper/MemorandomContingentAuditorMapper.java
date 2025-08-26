package com.afr.fms.Auditor.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Model.MGT.MemorandomAndContingent;

@Mapper
public interface MemorandomContingentAuditorMapper {

    
	
	@Select("select * from IS_Management_Audit where  id in  (select is_mgt_audit_id  from memorandom_contingent_mgt) AND auditor_id = #{user_id} and auditor_status != 1 and status = 1 Order By drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
            @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getDraftedMemorandomContingent(PaginatorPayLoad paginatorPayLoad);



    @Select("select * from IS_Management_Audit where id in   (select is_mgt_audit_id  from memorandom_contingent_mgt)  AND auditor_id = #{user_id} and auditor_status = 1 and review_status = 0 and status = 1 Order By drafted_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getPassedMemorandomContingent(PaginatorPayLoad paginatorPayLoad);



	
	@Select("select * from IS_Management_Audit where id in (SELECT is_mgt_audit_id from  memorandom_contingent_mgt)  AND auditor_id = #{user_id} and auditor_status = 1  and review_status = 2 and status = 1 Order By reviewer_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "memorandomAndContingent", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.MemorandomContingentAuditorMapper.getMemorandomByByISMGTAuditId")),

			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getRejectedAuditsForAuditor(PaginatorPayLoad paginatorPayLoad);

    @Select (" Select *  from memorandom_contingent_mgt   where  is_mgt_audit_id = #{is_mgt_audit_id} ")

    public  MemorandomAndContingent  getMemorandomByByISMGTAuditId(Long is_mgt_audit_id );


    @Insert("INSERT INTO memorandom_contingent_mgt (is_mgt_audit_id, memorandom_amount, difference, contingent_amount, cash_type, fcy) " +
            "VALUES (#{is_mgt_audit_id}, #{memorandom_amount}, #{difference}, #{contingent_amount}, #{cash_type}, #{fcy})")
    public void  createMemorandomContingent(MemorandomAndContingent memorandomAndContingent);

    @Update("UPDATE memorandom_contingent_mgt " +
            "SET memorandom_amount = #{memorandom_amount}, " +
            "difference = #{difference}, " +
            "contingent_amount = #{contingent_amount}, " +
            "cash_type = #{cash_type}, " +
            "fcy = #{fcy} " +
            "WHERE id = #{id}")
    public void  updateMemorandomContingent(MemorandomAndContingent memorandomAndContingent);


    
}
