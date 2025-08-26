
package com.afr.fms.Admin.Dashboard;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;

@Mapper
public interface AdminDashboardMapper {

        
        @Select("select * from role where role_position = #{role_position} order by code")
        public List<Role> getRolesByAuditType(String role_position);

        @Select("select * from IS_Management_Audit ")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = " com.afr.fms.Reviewer.Mapper.ReviewerDashBoardMapper.getISMAuditees"))
        })
        public List<AuditISM> getAudit();

        @Select("select * from IS_MGT_Auditee")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
        })
        public List<IS_MGT_Auditee> getDirectorates();

        @Select("select count(id)  from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and rectification_status = 4 and category = #{category}")
        public Integer getPartiallyRectifiedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(id)  from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and rectification_status = 1 and category = #{category}")
        public Integer getRectifiedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and review_status != 1 and auditor_status = 1 and category = #{category} ")
        public Integer getPassedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and review_status = 1 and approve_status != 1 and category = #{category} ")
        public Integer getReviewedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and approve_status = 1 and response_added !=1 and category = #{category} ")
        public Integer getApprovedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(id)  from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and complete_status = 1)  and response_added = 1 and rectification_status !=1 and category = #{category}")
        public Integer getRespondedAuditsPerAuditee(Long auditee_id, String category);

        // Polar Chart
        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and approve_status = 1 and id in (select IS_MGT_id from IS_MGT_Auditee where complete_status != 1)")
        public Integer getApprovedCount(Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and approve_status = 2")
        public Integer getApproverRejectedCount(Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and auditor_id = #{auditor_id} and rectification_status = 1")
        public Integer getRectifiedCount(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and rectification_status = 0")
        public Integer getRespondedCount(Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and auditor_id = #{auditor_id} and rectification_status = 2")
        public Integer getUnrectifiedCount(Long auditor_id);

        // Total Data
        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and approve_status = 1")
        public Integer getTotalApprovedCount(Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and approve_status = 2")
        public Integer getTotalApproverRejectedCount(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and  response_added = 1 ")
        public Integer getTotalRespondedCount(Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and auditor_id = #{auditor_id}   and rectification_status = 1")
        public Integer getTotalRectifiedCount(Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and auditor_id = #{auditor_id}   and rectification_status = 2")
        public Integer getTotalUnrectifiedCount(Long auditor_id);

        // Doughnut
        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and auditor_status = 1")
        public Integer getTotalPassedCounts(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and rectification_status = 4")
        public Integer getTotalPartialyRectifiedCounts(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and auditor_status = 3")
        public Integer getTotalFollowupRejectedCounts(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and review_status = 1")
        public Integer getTotalReviewedCounts(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and approve_status = 1")
        public Integer getTotalApprovedCounts(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and response_added = 1")
        public Integer getTotalRespondedCounts(Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where  auditor_id = #{auditor_id} and rectification_status = 1")
        public Integer getTotalRectifiedCounts(Long auditor_id);

        // Barchart per month current finding status
 

        @Select("select count(id) from [user] where category = #{category} and id in (select user_id from user_role where role_id in (select id from role where code like concat('%',#{code},'%') and role_position = #{category}) )")
        public Integer getUsersPerRoleandAudit(String category, String code);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and response_added =0 and approve_status = 2 and ( (SELECT MONTH (approver_rejected_date) AS Month) = #{month})")
        public Integer getApproverRejectedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and auditor_id = #{auditor_id} and response_added =1 and rectification_status = 1 and ( (SELECT MONTH (rectification_date) AS Month) = #{month})")
        public Integer getRectifiedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and auditor_id = #{auditor_id} and response_added = 1 and rectification_status = 2 and ( (SELECT MONTH (unrectification_date) AS Month) = #{month})")
        public Integer getUnrectifiedAuditsCountPerMonth(int month, Long auditor_id);

        // Stacked Barchart per month total finding status
        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and  approve_status = 1 and ( (SELECT MONTH (approved_date) AS Month) = #{month})")
        public Integer getTotalApprovedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and approve_status = 2 and ( (SELECT MONTH (approver_rejected_date) AS Month) = #{month})")
        public Integer getTotalApproverRejectedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and auditor_id = #{auditor_id} and response_added =1 and ( (SELECT MONTH (responded_date) AS Month) = #{month})")
        public Integer getTotalRespondedAuditsCountPerMonth(int month, Long auditor_id);

        // Linechart per month current finding status
        @Select("select count(id) from [user] u where u.category = #{category} and (u.branch_id in (select br.id from branch br where br.region_id=#{region_id}) or u.region_id =#{region_id})")
        public Integer getUsersPerRegion(String category, Long region_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id}  and auditor_status = 1 and ( (SELECT MONTH (passed_date) AS Month) = #{month})")
        public Integer getPassedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and review_status = 2  and ( (SELECT MONTH (reviewer_rejected_date) AS Month) = #{month})")
        public Integer getReviewerRejectedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and review_status = 1  and ( (SELECT MONTH (reviewed_date) AS Month) = #{month})")
        public Integer getReviewedAuditsCountPerMonth(int month, Long auditor_id);

        // Horizontal Barchart per month current finding status
        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and auditor_status in (0,1) and ( (SELECT MONTH (drafted_date) AS Month) = #{month})")
        public Integer getTotalDraftedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and auditor_status = 1 and ( (SELECT MONTH (passed_date) AS Month) = #{month})")
        public Integer getTotalPassedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and review_status = 2  and ( (SELECT MONTH (reviewer_rejected_date) AS Month) = #{month})")
        public Integer getTotalReviewerRejectedAuditsCountPerMonth(int month, Long auditor_id);

        @Select("select count(id) from IS_Management_Audit where auditor_id = #{auditor_id} and review_status = 1 and ( (SELECT MONTH (reviewed_date) AS Month) = #{month})")
        public Integer getTotalReviewedAuditsCountPerMonth(int month, Long auditor_id);

        // card
        @Select("select count(id)  from [user] where category = #{category}")
        public Integer getSystemUsersByCategory(String category);

        @Select("select count(id)  from user_tracker where status = #{status}")
        public Integer getUserLoginStatus(int status);




        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and auditor_status = 1 and review_status=0 ")
        public Integer getPassedCount(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and auditor_status = 1 ")
        public Integer getTotalPassedCount(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and review_status  = 2 and approve_status = 0")
        public Integer getRejectedCount(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and review_status  = 2")
        public Integer getTotalRejectedCount(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and review_status  = 1 and approve_status = 0")
        public Integer getReviewedCount(Long auditor_id);

        @Select("select count(id)  from IS_Management_Audit where auditor_id = #{auditor_id} and review_status  = 1")
        public Integer getTotalReviewedCount(Long auditor_id);

       // Radar Data
        @Select("select count(id) from [user] where status = #{status} and category = #{category}")
        public Integer getUserActiveStatusPerAudit(int status, String category);


        

}
