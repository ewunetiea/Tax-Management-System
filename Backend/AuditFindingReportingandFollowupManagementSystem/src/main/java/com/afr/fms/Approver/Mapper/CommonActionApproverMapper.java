package com.afr.fms.Approver.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Auditor.Entity.AuditISM;

@Mapper
public interface CommonActionApproverMapper {

	@Update("update IS_Management_Audit set approver_id = #{approver.id}, approve_status=1, approved_date = CURRENT_TIMESTAMP,finding_status='approved' where id = #{id}")
	public void approveAudit(AuditISM audit);

    @Update("update IS_Management_Audit set approver_id = #{approver.id}, approve_status=0 , finding_status= 'unapproved' where id = #{id}")
	public void cancelApprovalISM_Audit(AuditISM audit);

	@Update("update IS_Management_Audit set approver_id=#{approver.id}, approve_status = 2, approver_rejected_date = CURRENT_TIMESTAMP, finding_status= 'approver_rejected' where id=#{id}")
	public void rejectFinding(AuditISM audit);
    
}
