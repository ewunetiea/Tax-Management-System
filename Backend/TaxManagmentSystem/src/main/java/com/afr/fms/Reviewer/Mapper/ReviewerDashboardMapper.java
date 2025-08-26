package com.afr.fms.Reviewer.Mapper;

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
public interface ReviewerDashboardMapper {

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
        public Integer getRectifiedAudits(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 4"
                        + " where  isma.auditee_id = #{auditee_id} and  ima.category = #{category}")
        public Integer getPartiallyRectifiedAudits(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 0 and isma.complete_status = 1"
                        + " where  isma.auditee_id = #{auditee_id}   and ima.category = #{category}")
        public Integer getRespondedAudits(Long auditee_id, String category);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and review_status not in (1,2) and auditor_status = 1 and category = #{category} ")
        public Integer getPassedAuditsPerAuditee(Long auditee_id, String category);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and review_status = 1 and approve_status not in (1,2) and category = #{category} ")
        public Integer getReviewedAudits(Long auditee_id, String category);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1"
                        + " where  isma.auditee_id = #{auditee_id}   and ima.category = #{category} and ima.approve_status = 1")
        public Integer getApprovedAudits(Long auditee_id, String category);

        // Polar Chart
        @Select("select count(id) from IS_Management_Audit  where review_status=2 and reviewer_id = #{reviewer_id}")
        public Integer getRejectedFindings(Long reviewer_id);

        @Select("select count(id) from IS_Management_Audit  where approve_status=2 and   reviewer_id = #{reviewer_id}")
        public Integer getApproverRejectedFindings(Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2 "
                        + " where  ima.reviewer_id = #{reviewer_id} ")

        // @Select("select count(id) from IS_Management_Audit where
        // rectification_status=2 and reviewer_id = #{reviewer_id}")
        public Integer getUnrectifiedFindings(Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2 "
                        + " where  ima.reviewer_id = #{reviewer_id} ")

        // @Select("select count(id) from IS_Management_Audit where
        // rectification_status=3 and reviewer_id = #{reviewer_id} ")
        public Integer getFollowupRejectedFindings(Long reviewer_id);

        // Bar Chat
        @Select("select count(id)  from IS_Management_Audit  ism  where  ism.status=1 and ism.review_status not in (1,2) and ism.auditor_status = 1  and ( (SELECT MONTH (ism.passed_date) AS Month) = #{month})")
        public Integer getPendingFindingsPerMonth(int month, Long reviewer_id);

        @Select("select count(id)  from IS_Management_Audit ism  where ism.status=1 and ism.review_status = 1 and ism.approve_status not in (1,2) and ism.reviewer_id=#{reviewer_id} and ( (SELECT MONTH (ism.reviewed_date) AS Month) = #{month}) ")
        public Integer getReviewedFindingsPerMonth(int month, Long reviewer_id);

        @Select("select count(id)  from IS_Management_Audit ism  where ism.status=1 and ism.review_status=2 and  ism.reviewer_id=#{reviewer_id} and ( (SELECT MONTH (ism.reviewer_rejected_date) AS Month) = #{month})")
        public Integer getRejectedFindingsPerMonth(int month, Long reviewer_id);

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // approved_date, getDate()) between 1 and 10 and reviewer_id = #{reviewer_id}
        // and approve_status=1 and response_added = 0")
        // public Integer getFindings_1_10_Dayes(Long reviewer_id);

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // approved_date, getDate()) between 11 and 20 and reviewer_id = #{reviewer_id}
        // and approve_status=1 and response_added = 0")
        // public Integer getFindings_11_20_Dayes(Long reviewer_id);

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // approved_date, getDate()) between 21 and 30 and reviewer_id = #{reviewer_id}
        // and approve_status=1 and response_added = 0")
        // public Integer getFindings21_30_Dayes(Long reviewer_id);

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // approved_date, getDate()) between 31 and 40 and reviewer_id = #{reviewer_id}
        // and approve_status=1 and response_added =0")
        // public Integer getFindings31_40_Dayes(Long reviewer_id);

        // @Select("select count(id) from IS_Management_Audit where DATEDIFF(day,
        // approved_date, getDate()) > 40 and reviewer_id = #{reviewer_id} and
        // approve_status=1 and response_added =0")
        // public Integer getFindingsAbove_40_Dayes(Long reviewer_id);

        // @Select("select count(id) from IS_Management_Audit ism where status = 1 and
        // review_status not in (1,2) and category = #{category} and auditor_status =
        // 1")
        // public Integer getPendingFindings(String category);

        // Current Card Data
        @Select("select count(id)  from IS_Management_Audit ism  where ism.status=1 and ism.review_status=1 and ism.reviewer_id=#{reviewer_id} and approve_status not in (1,2) ")
        public Integer getReviewedFindings(Long reviewer_id);

        @Select("select count(id) from IS_Management_Audit ism  where status = 1 and category = #{category} and auditor_status = 1")
        public Integer getTotalPendingFindings(String category);

        @Select("select count(id) from IS_Management_Audit ism  where status = 1 and category = #{category} and auditor_status = 1 and review_status not in (1,2)")
        public Integer getPendingFindings(String category);

        @Select("select count(id) from IS_Management_Audit ism  where status = 1 and management = #{management} and auditor_status = 1")
        public Integer getTotalPendingFindingsMGT(String management);

        @Select("select count(id) from IS_Management_Audit ism  where status = 1 and management = #{management} and auditor_status = 1 and review_status not in (1,2)")
        public Integer getPendingFindingsMGT(String management);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1 "
                        + " where ima.status = 1 and ima.approve_status = 1 and  ima.reviewer_id=#{reviewer_id} ")
        public Integer getApprovedFindings(Long reviewer_id);

        // Total Card Data
        @Select("select count(id)  from IS_Management_Audit ism  where ism.status=1 and ism.review_status=1 and ism.reviewer_id=#{reviewer_id} ")
        public Integer getTotalReviewedFindings(Long reviewer_id);

        @Select("select count(id)  from IS_Management_Audit ism  where ism.status=1 and ism.approve_status=1 and  ism.reviewer_id=#{reviewer_id}")
        public Integer getTotalApprovedFindings(Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 1 "
                        + " where ima.status = 1 and ima.approve_status = 1 and  ima.reviewer_id=#{reviewer_id} ")

        // @Select("select count(id) from IS_Management_Audit ism where ism.status=1 and
        // ism.rectification_status=1 and ism.reviewer_id=#{reviewer_id}")
        public Integer getRectifiedFindings(Long reviewer_id);

        // Doughnut
        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 4 "
                        + " where ima.status = 1 and ima.approve_status = 1 and  ima.reviewer_id=#{reviewer_id} ")

        // @Select("select count(id) from IS_Management_Audit where
        // rectification_status=4 and reviewer_id = #{reviewer_id}")
        public Integer getPartiallyRectifiedFindings(Long reviewer_id);

        @Select("select count(id) from IS_Management_Audit  where approve_status=1 and   reviewer_id = #{reviewer_id}")
        public Integer getApprovedFindingsCount(Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 1 "
                        + " where ima.status = 1 and ima.approve_status = 1 and  ima.reviewer_id=#{reviewer_id} ")

        // @Select("select count(id) from IS_Management_Audit where
        // rectification_status=1 and reviewer_id = #{reviewer_id}")
        public Integer getFullyRectifiedFindings(Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 "
                        + " where ima.status = 1 and ima.approve_status = 1 and  ima.reviewer_id=#{reviewer_id} ")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status = 1) and reviewer_id =
        // #{reviewer_id}")
        public Integer getTotalRespondedFindings(Long reviewer_id);

        // Stacked Bar Chart
        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1 "
                        + " where  ima.approve_status = 1 and ima.reviewer_id = #{reviewer_id}  and ( (SELECT MONTH (ima.approved_date) AS Month) = #{month}) ")
        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where complete_status != 1) and status=1 and
        // approve_status = 1 and ( (SELECT MONTH (approved_date) AS Month) =
        // #{month})")
        public Integer getApprovedFindingsPerMonth(int month, Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 1 "
                        + " where  ima.reviewer_id = #{reviewer_id}  and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month}) ")

        // @Select("select count(id) from IS_Management_Audit ism where ism.status=1 and
        // ism.rectification_status=1 and ism.reviewer_id=#{reviewer_id} and ( (SELECT
        // MONTH (ism.rectification_date) AS Month) = #{month}) ")
        public Integer getRectifiedFindingsPerMonth(int month, Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 4 "
                        + " where  ima.reviewer_id = #{reviewer_id}  and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month}) ")

        // @Select("select count(id) from IS_Management_Audit ism where ism.status=1 and
        // ism.rectification_status=4 and ism.reviewer_id=#{reviewer_id} and ( (SELECT
        // MONTH (ism.rectification_date) AS Month) = #{month}) ")
        public Integer getPartiallyRectifiedFindingsPerMonth(int month, Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 and isma.rectification_status = 0 "
                        + " where  ima.reviewer_id = #{reviewer_id}  and ( (SELECT MONTH (isma.completed_date) AS Month) = #{month}) ")

        // @Select("select count(id) from IS_Management_Audit ism where ism.status=1 and
        // ism.id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1)
        // and ism.rectification_status=0 and ism.reviewer_id=#{reviewer_id} and (
        // (SELECT MONTH (ism.responded_date) AS Month) = #{month})")
        public Integer getRespondedFindingsPerMonth(int month, Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 2 "
                        + " where  ima.reviewer_id = #{reviewer_id}  and ( (SELECT MONTH (isma.unrectification_date) AS Month) = #{month}) ")

        // @Select("select count(id) from IS_Management_Audit ism where ism.status=1 and
        // ism.rectification_status=2 and ism.reviewer_id=#{reviewer_id} and ( (SELECT
        // MONTH (ism.unrectification_date) AS Month) = #{month})")
        public Integer getUnrectifiedFindingsPerMonth(int month, Long reviewer_id);

        // Horizontal Bar Chart
        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id "
                        + " where  ima.approve_status = 1 and ima.reviewer_id = #{reviewer_id}  and ( (SELECT MONTH (ima.approved_date) AS Month) = #{month}) ")
        // @Select("select count(id) from IS_Management_Audit ism where ism.status=1 and
        // ism.approve_status=1 and ( (SELECT MONTH (ism.approved_date) AS Month) =
        // #{month})")
        public Integer getTotalApprovedFindingsPerMonth(int month, Long reviewer_id);

        @Select("select count(ima.id)  from IS_Management_Audit ima" +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 "
                        + " where  ima.reviewer_id = #{reviewer_id}  and ( (SELECT MONTH (isma.completed_date) AS Month) = #{month}) ")

        // @Select("select count(id) from IS_Management_Audit ism where ism.status=1 and
        // ism.id in (select IS_MGT_id from IS_MGT_Auditee where complete_status = 1)
        // and ism.reviewer_id=#{reviewer_id} and ( (SELECT MONTH (ism.responded_date)
        // AS Month) = #{month})")
        public Integer getTotalRespondedFindingsPerMonth(int month, Long reviewer_id);

        // Radar
        @Select("select count(id) from IS_Management_Audit where  DATEDIFF(day, reviewed_date, getDate()) between #{ range[0]} and #{range[1]} and reviewer_id = #{reviewer_id} and review_status = 1 and approve_status  not in (1,2)")
        public Integer getReviewedFindingsAge(Long reviewer_id, int range[]);

        @Select("select count(ism.id) from IS_Management_Audit ism " +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.complete_status != 1 "
                        + "where (DATEDIFF(day, ism.approved_date, getDate()) between #{range[0]} and #{range[1]}) and ism.reviewer_id = #{reviewer_id} and ism.approve_status = 1")
        public Integer getApprovedFindingsAge(Long reviewer_id, int range[]);

        @Select("select count(id) from IS_Management_Audit where  DATEDIFF(day, reviewed_date, getDate()) > 40 and reviewer_id = #{reviewer_id} and review_status = 1 and approve_status not in (1,2) ")
        public Integer getReviewedFindingsAbove40Days(Long reviewer_id);

        @Select("select count(ism.id) from IS_Management_Audit ism "
                        + " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ism.id and isma.complete_status != 1 "
                        + "where (DATEDIFF(day, ism.approved_date, getDate()) > 40) and ism.reviewer_id = #{reviewer_id} and ism.approve_status = 1")
        public Integer getApprovedFindingsAbove40Days(Long reviewer_id);

}