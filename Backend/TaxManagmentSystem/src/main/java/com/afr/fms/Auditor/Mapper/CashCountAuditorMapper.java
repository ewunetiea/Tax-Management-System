package com.afr.fms.Auditor.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.CashCount;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@Mapper
public interface CashCountAuditorMapper {

    @Update("update IS_Management_Audit set management = #{management}, finding=#{finding}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = #{finding_date}, is_edited = 1 where id=#{id}")
    public void updateMGTFinding(AuditISM audit);

    @Update("update IS_Management_Audit set management = #{management}, finding=#{finding}, finding_detail = #{finding_detail}, impact=#{impact}, recommendation=#{recommendation}, amount=#{amount}, cash_type=#{cash_type}, fcy=#{fcy},  risk_level=#{risk_level}, finding_date = #{finding_date}, is_edited = 1 where id=#{id}")
    public void updateMGTFindingandFindingDetail(AuditISM audit);

    @Insert("insert into cash_count_mgt (is_mgt_audit_id, accountable_staff, actual_cash_count, trial_balance, difference )  values (#{is_mgt_audit_id},  #{accountable_staff}, #{actual_cash_count}, #{trial_balance}, #{difference})")
    public void createCashCount(CashCount cashCount);

    @Update(" UPDATE cash_count_mgt SET is_mgt_audit_id = #{is_mgt_audit_id},  accountable_staff = #{accountable_staff},  actual_cash_count = #{actual_cash_count}, trial_balance = #{trial_balance}, difference = #{difference} WHERE id = #{id}")
    public void updateCashCount(CashCount cashCount);

    @Select("select  * from cash_count_mgt where is_mgt_audit_id = #{is_management_audit_id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
    })
    public CashCount getCashCountByManagementId(Long is_management_audit_id);

    @Select("select ima.* from IS_Management_Audit ima " +
            "INNER JOIN cash_count_mgt ccm on ima.id = ccm.is_mgt_audit_id " +
            "where ima.auditor_id = #{user_id} and ima.auditor_status != 1 and ima.status = 1 Order By ima.drafted_date DESC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
            @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
            @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
            @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
    })
    public List<AuditISM> getAuditsOnDrafting(PaginatorPayLoad paginatorPayload);

    @Select("select ima.* from IS_Management_Audit ima " +
            "INNER JOIN cash_count_mgt ccm on ima.id = ccm.is_mgt_audit_id " +
            "where ima.auditor_id = #{user_id} and ima.auditor_status = 1 and ima.status = 1 and ima.review_status = 0 Order By ima.passed_date DESC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
            @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
            @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
            @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
    })
    public List<AuditISM> getAuditsOnProgress(PaginatorPayLoad paginatorPayload);

    @Select("select ima.* from IS_Management_Audit ima " +
            "INNER JOIN cash_count_mgt ccm on ima.id = ccm.is_mgt_audit_id " +
            "where ima.auditor_id = #{user_id} and ima.auditor_status = 1 and ima.status = 1 and ima.review_status = 2 Order By ima.reviewer_rejected_date DESC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "reviewer", column = "reviewer_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "approver", column = "approver_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
            @Result(property = "cashCount", column = "id", one = @One(select = "com.afr.fms.Auditor.Mapper.CashCountAuditorMapper.getCashCountByManagementId")),
            @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees")),
            @Result(property = "change_tracker_ISM", column = "id", many = @Many(select = "com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMMapper.getChanges")),
            @Result(property = "file_urls", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.UploadFileISMMapper.getFileUrlsByAuditID"))
    })
    public List<AuditISM> getRejectedFindings(PaginatorPayLoad paginatorPayload);

}
