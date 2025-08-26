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
public interface AbnormalBalanceApproverMappper {

    @Select("select ima.* from IS_Management_Audit ima " +
            "INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id " +
            "where ima.category = #{category} and ima.review_status = 1 and ima.status = 1 and ima.approve_status = 0 Order By ima.reviewed_date DESC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
            @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
            @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
            @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
    })
    public List<AuditISM> getPendingAudits(PaginatorPayLoad paginatorPayload);

    @Select("select ima.* from IS_Management_Audit ima " +
            "INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id " +
            "INNER JOIN IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id " +
            "where ima.approver_id = #{user_id} and ima.approve_status = 1 and isma.division_assigned = 0 order by ima.approved_date DESC ")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
            @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
            @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
            @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
    })
    public List<AuditISM> getApprovedAudits(PaginatorPayLoad paginatorPayload);

    @Select("select ima.* from IS_Management_Audit ima " +
            "INNER JOIN abnormal_balance_mgt abm on ima.id = abm.is_management_audit_id " +
            "where ima.approver_id = #{user_id} and ima.approve_status = 2 order by ima.approver_rejected_date DESC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "abnormalBalance", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.AbnormalBalanceAuditorMapper.getAbnormalBalanceByManagementId")),
            @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
            @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
            @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
    })
    public List<AuditISM> getRejectedAudits(PaginatorPayLoad paginatorPayload);
    

}
