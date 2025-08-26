package com.afr.fms.Approver.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;


@Mapper
public interface AssetLiabilityApproverMapper {
    
 @Select("select * from IS_Management_Audit where id in (SELECT is_mgt_audit_id from  asset_liability_mgt)  AND category = #{category} and review_status = 1  and approve_status = 0  and status = 1  Order By reviewed_date DESC ")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getPendingAudits(PaginatorPayLoad paginatorPayLoad);


    // @Select("select * from IS_Management_Audit where  id in (SELECT is_mgt_audit_id from  asset_liability_mgt)  AND review_status = 1 and reviewer_id=#{user_id} and status = 1 and approve_status = 0  Order By reviewed_date DESC ")

    @Select("select ima.*, isma.auditee_id as directorate_id from IS_Management_Audit  ima\r\n" + //
    "inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned != 1\r\n" + //
    "where ima.id in (select is_mgt_audit_id  from asset_liability_mgt) AND ima.approver_id= #{user_id} and ima.approve_status=1 order by ima.approved_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),

			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getApprovedAudits(PaginatorPayLoad paginatorPayLoad);



	@Select("select * from IS_Management_Audit where id in (SELECT is_mgt_audit_id from  asset_liability_mgt) AND  review_status = 1 and approver_id= #{user_id} and status = 1 and approve_status = 2  Order By approver_rejected_date DESC")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
			@Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
			@Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "assetAndLiability", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AssetLiabilityAuditorMapper.getAssetAndLiabilityByISMGId")),

			@Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
			@Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))

	})
	public List<AuditISM> getRejectedFindings(PaginatorPayLoad paginatorPayLoad);
    
}
