
package com.afr.fms.Approver.Dashboard;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;

@Mapper
public interface ApproverDashboardMapper {

        @Select("select * from IS_Management_Audit ")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = " com.afr.fms.Reviewer.Mapper.ReviewerDashBoardMapper.getISMAuditees"))
        })
        public List<AuditISM> getAudit();

        // @Select("select * from IS_MGT_Auditee")
        @Select("select isma.* from IS_MGT_Auditee isma " + 
                        "inner join IS_Management_Audit ima on isma.IS_MGT_id = ima.id and ima.category = #{category}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
        })
        public List<IS_MGT_Auditee> getDirectorates( String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 1"
                        + " where  isma.auditee_id = #{auditee_id} and  ima.category = #{category}")
        public Integer getRectifiedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 4"
                        + " where  isma.auditee_id = #{auditee_id} and  ima.category = #{category}")
        public Integer getPartiallyRectifiedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 0 and isma.complete_status = 1"
                        + " where  isma.auditee_id = #{auditee_id}   and ima.category = #{category}")
        public Integer getRespondedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1"
                        + " where  isma.auditee_id = #{auditee_id}   and ima.category = #{category} and ima.approve_status = 1")
        public Integer getApprovedAuditsPerAuditee(Long auditee_id, String category);

        // Polar Chart
        @Select("select count(id)  from IS_Management_Audit where approver_id = #{id} and approve_status = 2 and category = #{category}")
        public Integer getRejectedCount(User user);

        // @Select("select count(ima.id) from IS_Management_Audit ima" +
        // " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and
        // isma.complete_status = 0"
        // + " where ima.approver_id = #{id} and ima.approve_status = 1")

        @Select("select count(id)  from IS_Management_Audit where approver_id = #{id} and approve_status = 1 and category = #{category}")
        public Integer getResponsePendingCount(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.rectification_status = 2"
                        + " where  ima.approver_id = #{id}")

        // @Select("select count(id) from IS_Management_Audit where approver_id = #{id}
        // and rectification_status = 2 and category = #{category}")
        public Integer getUnrectifiedCount(User user);

        // @Select("select count(ima.id) from IS_Management_Audit ima" +
        // " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and
        // isma.rectification_status = 4"
        // + " where ima.approver_id = #{id}")

        @Select("select count(id)  from IS_Management_Audit where review_status = 1 and category = #{category} and approve_status = 0 ")
        public Integer getTotalUnapprovedCount(User user);

        // Doughnout Chart

        @Select("select count(id)  from IS_Management_Audit where approver_id = #{id} and (approver_rejected_date is NOT NULL) and category = #{category}")
        public Integer getTotalRejectedCounts(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status = 1 and isma.rectification_status = 0"
                        + " where ima.approver_id = #{id} and ima.approve_status = 1")

        // @Select("select count(id) from IS_Management_Audit where approver_id = #{id}
        // and id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1)
        // and response_added = 1 and category = #{category}")
        public Integer getTotalRespondedCounts(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status != 1 and isma.rectification_status = 0"
                        + " where ima.approver_id = #{id} and ima.approve_status = 1")

        // @Select("select count(id) from IS_Management_Audit where approver_id = #{id}
        // and id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1)
        // and response_added = 1 and category = #{category}")
        public Integer getAuditeeHandCounts(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status not in (1,4)"
                        + " where  ima.approver_id = #{id}")

        // @Select("select count(id) from IS_Management_Audit where approver_id = #{id}
        // and (unrectification_date is NOT NULL) and category = #{category}")
        public Integer getTotalUnrectifiedCounts(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.rectification_status = 4"
                        + " where  ima.approver_id = #{id}")

        // @Select("select count(id) from IS_Management_Audit where approver_id = #{id}
        // and rectification_status = 4 and category = #{category}")
        public Integer getPartiallyRectifiedCounts(User user);

        // Stacked Bar Chart
        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status = 1 and isma.rectification_status = 0"
                        + " where ima.approver_id = #{user.id}  and ima.category = #{user.category} and ( (SELECT MONTH (isma.completed_date) AS Month) = #{month})")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and approver_id =
        // #{user.id} and category = #{user.category} and rectification_status = 0 and (
        // (SELECT MONTH (responded_date) AS Month) = #{month})")
        public Integer getRespondedAuditsCountPerMonth(int month, User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 4 "
                        + " where   ima.approver_id = #{user.id}  and ima.category = #{user.category} and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month}) ")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and approver_id =
        // #{user.id} and category = #{user.category} and rectification_status = 4 and (
        // (SELECT MONTH (rectification_date) AS Month) = #{month})")
        public Integer getPartiallyRectifiedAuditsCountPerMonth(int month, User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 1 and isma.complete_status = 1"
                        + " where  ima.approver_id = #{user.id}  and ima.category = #{user.category} and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month}) ")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and approver_id =
        // #{user.id} and category = #{user.category} and rectification_status = 1 and (
        // (SELECT MONTH (rectification_date) AS Month) = #{month})")
        public Integer getRectifiedAuditsCountPerMonth(int month, User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2 "
                        + " where  ima.approver_id = #{user.id}  and ima.category = #{user.category} and ( (SELECT MONTH (isma.unrectification_date) AS Month) = #{month}) ")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and approver_id =
        // #{user.id} and category = #{user.category} and rectification_status = 2 and (
        // (SELECT MONTH (unrectification_date) AS Month) = #{month})")
        public Integer getUnrectifiedAuditsCountPerMonth(int month, User user);

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and approver_id =
        // #{user.id} and category = #{user.category} and rectification_status = 3 and (
        // (SELECT MONTH (followup_rejected_date) AS Month) = #{month})")
        // public Integer getFollowupRejectedAuditsCountPerMonth(int month, User user);

        // Bar Chart
        @Select("select count(id) from IS_Management_Audit where category = #{category} and review_status = 1 and approve_status = 0 and ( (SELECT MONTH (reviewed_date) AS Month) = #{month})")
        public Integer getPendingAuditsCountPerMonth(int month, String category);

        @Select("select count(id) from IS_Management_Audit where approver_id = #{user.id} and response_added =0  and category = #{user.category} and approve_status = 1 and ( (SELECT MONTH (approved_date) AS Month) = #{month})")
        public Integer getApprovedAuditsCountPerMonth(int month, User user);

        @Select("select count(id) from IS_Management_Audit where approver_id = #{user.id} and approve_status = 2  and category = #{user.category} and review_status=1 and ( (SELECT MONTH (approver_rejected_date) AS Month) = #{month})")
        public Integer getRejectedCountPerMonth(int month, User user);

        // Horizontal Chart
        @Select("select count(id) from IS_Management_Audit where category = #{category} and review_status = 1 and ( (SELECT MONTH (reviewed_date) AS Month) = #{month})")
        public Integer getTotalPendingAuditsCountPerMonth(int month, String category);

        @Select("select count(id) from IS_Management_Audit where approver_id = #{user.id} and approve_status = 1  and category = #{user.category} and ( (SELECT MONTH (approved_date) AS Month) = #{month})")
        public Integer getTotalApprovedAuditsCountPerMonth(int month, User user);

        @Select("select count(id) from IS_Management_Audit where approver_id = #{user.id} and approve_status = 2 and category = #{user.category} and ( (SELECT MONTH (approver_rejected_date) AS Month) = #{month})")
        public Integer getRejectedAuditsCountPerMonth(int month, User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status = 1 "
                        + " where ima.approver_id = #{user.id}  and ima.category = #{user.category} and ( (SELECT MONTH (isma.completed_date) AS Month) = #{month})")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and approver_id =
        // #{user.id} and category = #{user.category} and ( (SELECT MONTH
        // (responded_date) AS Month) = #{month})")
        public Integer getTotalRespondedAuditsCountPerMonth(int month, User user);

        // Card
        // @Select("select count(id) from IS_Management_Audit where category =
        // #{category} and review_status=1 ")
        @Select("select count(id)  from IS_Management_Audit where category = #{category}  and approve_status  = 0 and review_status = 1")
        public Integer getTotalPendingCount(String category);

        // @Select("select count(id) from IS_Management_Audit where approver_id = #{id}
        // and category = #{category} and approve_status=1 ")
        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status != 1 and isma.rectification_status = 0 "
                        + " where ima.category = #{category} and ima.approver_id = #{id} and ima.approve_status = 1")
        public Integer getTotalApprovedFindingsCount(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status = 1 and isma.rectification_status = 0"
                        + " where ima.approver_id = #{id} and ima.approve_status = 1")

        // @Select("select count(id) from IS_Management_Audit where approver_id = #{id}
        // and id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1)
        // and response_added = 1 and category = #{category}")
        public Integer getRespondedCountsCard(User user);

        // Current Data
        @Select("select count(id)  from IS_Management_Audit where category = #{category} and response_added = 0 and approve_status  = 0 and review_status = 1")
        public Integer getPendingCount(String category);

        @Select("select count(id) from IS_Management_Audit where approver_id = #{id} and category = #{category} and approve_status = 1")
        public Integer getApprovedCount(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status = 1 "
                        + " where ima.category = #{category} and ima.approver_id = #{id} and ima.approve_status = 1")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and response_added =
        // 1 and category = #{category} and approver_id = #{id} and approve_status = 1")
        public Integer getResponsedCount(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 1 and isma.complete_status = 1"
                        + " where  ima.approver_id = #{id}  and ima.category = #{category} ")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and approver_id =
        // #{id} and category = #{category} and response_added =1 and
        // rectification_status = 1")
        public Integer getRectifiedCount(User user);

        // Radar Data

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1"
                        + " where   ima.approver_id = #{user.id} and ima.category = #{user.category} and ima.approve_status=1 and DATEDIFF(day, ima.approved_date, getDate()) between #{ range[0]} and #{range[1]}")

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // approved_date, getDate()) between #{ range[0]} and #{range[1]} and
        // approver_id = #{user.id} and category = #{user.category} and approve_status=1
        // and response_added = 0")
        public Integer getApprovedFindingsAge(User user, int range[]);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status = 1 and isma.rectification_status = 0 "
                        + " where DATEDIFF(day, isma.completed_date, getDate()) between #{range[0]} and #{range[1]} and ima.approver_id = #{user.id} and ima.category = #{user.category}")

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // responded_date, getDate()) between #{range[0]} and #{range[1]} and
        // approver_id = #{user.id} and category = #{user.category} and response_added =
        // 1 and rectification_status = 0")
        public Integer getRespondedFindingsAge(User user, int range[]);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1"
                        + " where  ima.approver_id = #{id} and ima.category = #{category} and ima.approve_status=1 and DATEDIFF(day, ima.approved_date, getDate()) > 40")

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // approved_date, getDate()) > 40 and approver_id = #{id} and category =
        // #{category} and approve_status=1 and response_added =0")
        public Integer getApprovedFindingsAbove40Dayes(User user);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and  isma.complete_status = 1 and isma.rectification_status = 0 "
                        + " where DATEDIFF(day, isma.completed_date, getDate()) > 40 and ima.approver_id = #{id} and ima.category = #{category}")

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // responded_date, getDate()) > 40 and approver_id = #{id} and category =
        // #{category} and response_added = 1 and id in (select IS_MGT_id from
        // IS_MGT_Auditee where complete_status = 1) and rectification_status =0")
        public Integer getRespondedFindingsAbove40Dayes(User user);

}
