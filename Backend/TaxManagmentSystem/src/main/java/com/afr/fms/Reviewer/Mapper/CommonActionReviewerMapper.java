package com.afr.fms.Reviewer.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Auditor.Entity.AuditISM;

@Mapper
public interface CommonActionReviewerMapper {
    	@Update("update IS_Management_Audit set  reviewer_id=#{reviewer.id}, review_status = 1 , reviewed_date= CURRENT_TIMESTAMP, finding_status= 'reviewed' where id=#{id}")
	public void reviewFindings(AuditISM audit);

	@Update("update IS_Management_Audit set  reviewer_id=#{reviewer.id}, review_status = 0, approve_status = 0 , finding_status= 'Unreviewed'  where id=#{id}")
	public void unReviewISM_Audit_Findings(AuditISM audit);

	@Update("update IS_Management_Audit set reviewer_id=#{reviewer.id}, review_status = 2, reviewer_rejected_date = CURRENT_TIMESTAMP, finding_status= 'reviewer_rejected' where id=#{id}")
	public void cancelFinding(AuditISM audit);
    
}
