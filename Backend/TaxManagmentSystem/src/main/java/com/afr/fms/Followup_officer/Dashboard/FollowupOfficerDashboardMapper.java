
package com.afr.fms.Followup_officer.Dashboard;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;

@Mapper
public interface FollowupOfficerDashboardMapper {

        @Select("select * from IS_Management_Audit ")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = " com.afr.fms.Reviewer.Mapper.ReviewerDashBoardMapper.getISMAuditees"))
        })
        public List<AuditISM> getAudit();

        @Select("select isma.* from IS_MGT_Auditee isma " +
                        "inner join IS_Management_Audit ima on isma.IS_MGT_id = ima.id and ima.category = #{category}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
        })
        public List<IS_MGT_Auditee> getDirectorates(String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 1"
                        + " where  isma.auditee_id = #{auditee_id} and  ima.category = #{category}")
        public Integer getRectifiedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 4"
                        + " where  isma.auditee_id = #{auditee_id} and  ima.category = #{category}")
        public Integer getPartiallyRectifiedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2"
                        + " where  isma.auditee_id = #{auditee_id} and   ima.category = #{category}")
        public Integer getRejectedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2"
                        + " where  isma.auditee_id = #{auditee_id} and   ima.category = #{category}")
        public Integer getUnrectifiedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 0 and isma.complete_status = 1"
                        + " where  isma.auditee_id = #{auditee_id}   and ima.category = #{category}")
        public Integer getRespondedAuditsPerAuditee(Long auditee_id, String category);

        // polar
        @Select("select count(id) from IS_Management_Audit where category = #{category} and review_status = 1 and approve_status = 0")
        public Integer getReviewedCount(String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        "   inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1" +
                        "   where  ima.approve_status = 1  and ima.category = #{category}")
        public Integer getApprovedCount(String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        "   inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 and isma.rectification_status = 0"
                        +
                        "   where  ima.category = #{category}")
        public Integer getRespondedCount(String category);

        @Select("select count(ima.id) from IS_Management_Audit ima    " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id  = ima.id and isma.complete_status = 1 and isma.rectification_status = 1    "
                        + //
                        "where ima.followup_id = #{followup_id} ")
        public Integer getRectifiedCountCurrent(Long followup_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id  = ima.id and isma.rectification_status = 4 "
                        + //
                        "where ima.followup_id = #{followup_id} ")
        public Integer getPartiallyRectifiedCountCurrent(Long followup_id);

        // Doughnut
        @Select("select count(id)  from IS_Management_Audit where category = #{category} and review_status = 1")
        public Integer getTotalReviewedCounts(String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima " + //
                        "  inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id "
                        +
                        "  where  ima.approve_status = 1  and ima.category = #{category}")
        public Integer getTotalApprovedCounts(String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        "   inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 "
                        +
                        "   where  ima.category = #{category}")
        public Integer getTotalRespondedCounts(String category);

        // Stacked Barchart per month current finding status
        @Select("select count(ima.id) from IS_Management_Audit ima    " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id  = ima.id and isma.complete_status = 1 and isma.rectification_status = 0    "
                        + //
                        "where  ima.category = #{category} and ( (SELECT MONTH (isma.completed_date) AS Month) = #{month})")
        public Integer getPendingAuditsCountPerMonth(int month, String category);

        @Select("select count(ima.id) from IS_Management_Audit ima    " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id  = ima.id and isma.complete_status = 1 and isma.rectification_status = 1    "
                        + //
                        "where ima.followup_id = #{followup_id} and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month})")
        public Integer getRectifiedAuditsCountPerMonth(int month, Long followup_id);

        @Select("select count(ima.id) from IS_Management_Audit ima    " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id  = ima.id and isma.rectification_status = 4    "
                        +
                        "where ima.followup_id = #{followup_id} and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month})")
        public Integer getPartiallyRectifiedAuditsCountPerMonth(int month, Long followup_id);

        @Select("select count(ima.id) from IS_Management_Audit ima    " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id  = ima.id and isma.rectification_status = 2    "
                        + //
                        "where ima.followup_id = #{followup_id} and ( (SELECT MONTH (isma.unrectification_date) AS Month) = #{month})")
        public Integer getRejectedAuditsCountPerMonth(int month, Long followup_id);

        @Select("select count(ima.id) from IS_Management_Audit ima    " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id  = ima.id and  isma.rectification_status = 2    "
                        + //
                        "where ima.followup_id = #{followup_id} and ( (SELECT MONTH (isma.unrectification_date) AS Month) = #{month})")
        public Integer getUnrectifiedAuditsCountPerMonth(int month, Long followup_id);

        // Horizontal Barchart per month total finding status

        @Select("select count(id) from IS_Management_Audit where category = #{category} and review_status = 1 and ( (SELECT MONTH ( reviewed_date) AS Month) = #{month})")
        public Integer getTotalReviewedAuditsCountPerMonth(int month, String category);

        @Select("select count(id) from IS_Management_Audit where category = #{category} and  approve_status = 1 and ( (SELECT MONTH (approved_date) AS Month) = #{month})")
        public Integer getTotalApprovedAuditsCountPerMonth(int month, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima " +
                        "    inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 " +
                        "   where  ima.category = #{category} and ( (SELECT MONTH (isma.completed_date) AS Month) = #{month})")
        public Integer getTotalRespondedAuditsCountPerMonth(int month, String category);

        // card
        @Select("select count(ima.id)  from IS_Management_Audit ima " +
                        "  inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 and isma.rectification_status = 0 "
                        +
                        "  where  ima.category = #{category}")
        public Integer getPendingCount(String category);

        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2 "
                        +
                        " where  ima.followup_id = #{followup_id} ")
        public Integer getRejectedCount(Long followup_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 1 "
                        +
                        " where  ima.followup_id = #{followup_id} ")
        public Integer getRectifiedCount(Long followup_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2 "
                        +
                        " where  ima.followup_id = #{followup_id} ")
        public Integer getUnrectifiedCount(Long followup_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " +
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2 " +
                        "where  DATEDIFF(day, isma.unrectification_date, getDate()) between #{ range[0]} and #{range[1]} and ima.followup_id = #{followup_id}")
        public Integer getRejectedFindingsAge(Long followup_id, int range[]);

        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 0 and isma.complete_status = 1 "
                        + //
                        "where  DATEDIFF(day, isma.completed_date, getDate()) between #{ range[0]} and #{range[1]} and ima.category = #{category}")
        public Integer getRespondedFindingsAge(String category, int range[]);

        @Select("select count(ima.id) from IS_Management_Audit ima " +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2 "
                        +
                        " where  DATEDIFF(day, isma.unrectification_date, getDate()) > 40 and ima.followup_id = #{followup_id}")
        public Integer getRejectedFindingsAbove40Days(Long followup_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 0 and isma.complete_status = 1 "
                        +
                        " where  DATEDIFF(day, isma.completed_date, getDate()) > 40 and ima.category = #{category}")
        public Integer getRespondedFindingsAbove40Days(String category);

}
