
package com.afr.fms.Auditee.AuditeeDashboard;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;

@Mapper
public interface AuditeeDashboardMapper {

        @Select("select distinct division_id from Audtiee_Division_ISM where IS_MGT_Auditee_id in (select id from IS_MGT_Auditee where auditee_id = #{IS_MGT_Auditee_id} ) ")
        @Results(value = {
                        @Result(property = "division_id", column = "division_id"),
                        @Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
        })
        public List<AuditeeDivisionISM> getDivisions(Long IS_MGT_Auditee_id);

        // polar
        @Select("select count(ismgt.id) from IS_Management_Audit ismgt  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ismgt.id "
                        // + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted = 1 "
                        + //
                        "  where isma.auditee_id  = #{auditee_id}")
        public Integer getRectifiedCount(Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 2 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        + "where isma.auditee_id = #{auditee_id}")
        public Integer getUnrectifiedCount(Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 4 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        + "where isma.auditee_id = #{auditee_id}")
        public Integer getPartiallyRectifiedCount(Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 4 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        + "where isma.auditee_id = #{auditee_id}")
        public Integer getTotalPartiallyRectifiedCount(Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 2 "
                        // + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        + "where isma.auditee_id = #{auditee_id}")
        public Integer getRejectedCount(Long auditee_id);

        // stacked bar chart
        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee ) and approve_status = 1 and division_assigned
        // !=1 and ( (SELECT MONTH (approved_date) AS Month) = #{month})")
        // public Integer getPendingAuditsCountPerDivision(int month, Long auditee_id);

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and
        // complete_status != 1) and response_added = 1 and ( (SELECT MONTH
        // (responded_date) AS Month) = #{month})")
        // public Integer getRespondedAuditsCountPerDivision(int month, Long
        // auditee_id);

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and
        // complete_status = 1) and response_added = 1 and rectification_status = 0 and
        // ( (SELECT MONTH (responded_date) AS Month) = #{month})")
        // public Integer getFinishedAuditsCountPerDivision(int month, Long auditee_id);

        @Select("   select count(ima.id) from IS_Management_Audit ima  "
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 0 "
                        + "AND isma.complete_status != 1 "
                        + "AND isma.division_assigned = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        + "   where adi.division_id = #{division_id} and isma.auditee_id = #{auditee_id} ")
        public Integer getOnProgressAuditsCountPerDivision(Long division_id, Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        + "AND adi.auditee_submitted = 1 "
                        + "   where adi.division_id = #{division_id} and isma.auditee_id = #{auditee_id} ")
        public Integer getRespondedAuditsCountPerDivision(Long division_id, Long auditee_id);

        @Select("   select count(ima.id) from IS_Management_Audit ima  "
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 4 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        + "   where adi.division_id = #{division_id}  and isma.auditee_id = #{auditee_id} ")
        public Integer getPartiallyRectifiedAuditsCountPerDivision(Long division_id, Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 2 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        + "   where adi.division_id = #{division_id} and isma.auditee_id = #{auditee_id} ")
        public Integer getUnrectifiedAuditsCountPerDivision(Long division_id, Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        // + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted = 1 "
                        + "   where adi.division_id = #{division_id} and isma.auditee_id = #{auditee_id} ")
        public Integer getFinishedAuditsCountPerDivision(Long division_id, Long auditee_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and approve_status = 1 and division_assigned !=1 and ( (SELECT MONTH (approved_date) AS Month) = #{month})")
        public Integer getPendingAuditsCountPerMonth(int month, Long auditee_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and complete_status != 1) and response_added = 1 and ( (SELECT MONTH (responded_date) AS Month) = #{month})")
        public Integer getRespondedAuditsCountPerMonth(int month, Long auditee_id);

        @Select("select count(id) from IS_Management_Audit where id in (select IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and complete_status = 1) and response_added = 1 and rectification_status = 0 and ( (SELECT MONTH (responded_date) AS Month) = #{month})")
        public Integer getFinishedAuditsCountPerMonth(int month, Long auditee_id);

        // Line Chart
        @Select("select count(ima.id) from IS_Management_Audit ima "
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        // + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted = 1 "
                        + "  where isma.auditee_id = #{auditee_id} and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month})")
        public Integer getRectifiedAuditsCountPerMonth(int month, Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima   "
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 4 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        +
                        "   where isma.auditee_id = #{auditee_id} and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month})")
        public Integer getPartiallyRectifiedAuditsCountPerMonth(int month, Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  "
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 2 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        +
                        " where isma.auditee_id = #{auditee_id} and ( (SELECT MONTH (isma.unrectification_date) AS Month) = #{month})")
        public Integer getUnrectifiedAuditsCountPerMonth(int month, Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  "
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 2 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        +
                        " where isma.auditee_id = #{auditee_id} and ( (SELECT MONTH (isma.unrectification_date) AS Month) = #{month})")
        public Integer getRejectedAuditsCountPerMonth(int month, Long auditee_id);

        // horizontal bar chart

        @Select(" select count(ismgt.id) from IS_Management_Audit ismgt" +

                        "   inner join  IS_MGT_Auditee isma on isma.IS_MGT_id = ismgt.id "
                        +

                        "   where approve_status = 1 and isma.auditee_id  = #{auditee_id} and ( (SELECT MONTH (ismgt.approved_date) AS Month) = #{month})")

        public Integer getTotalPendingAuditsCountPerMonth(int month, Long auditee_id);

        @Select("select count(ismgt.id) from IS_Management_Audit ismgt " +

                        "   inner join  IS_MGT_Auditee isma on isma.IS_MGT_id = ismgt.id and isma.division_assigned = 1 "
                        +

                        "   inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.submitted_auditee = 1 "
                        +

                        "   where isma.auditee_id  = #{auditee_id} and ( (SELECT MONTH (adi.response_subimtted_date) AS Month) = #{month})")
        public Integer getTotalRespondedAuditsCountPerMonth(int month, Long auditee_id);

        @Select("select count(ismgt.id) from IS_Management_Audit ismgt " +

                        "   inner join  IS_MGT_Auditee isma on isma.IS_MGT_id = ismgt.id and isma.division_assigned = 1 and isma.complete_status = 1 "
                        +

                        " where isma.auditee_id  = #{auditee_id} and ( (SELECT MONTH (isma.completed_date) AS Month) = #{month})")
        public Integer getTotalFinishedAuditsCountPerMonth(int month, Long auditee_id);

        // card
        @Select("  select count(ismgt.id) from IS_Management_Audit ismgt  " + //
                        "  inner join  IS_MGT_Auditee isma on isma.IS_MGT_id = ismgt.id and isma.division_assigned = 0  "
                        + //
                        "  where approve_status = 1 and isma.auditee_id  = #{auditee_id}")
        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id}) and
        // approve_status = 1 and division_assigned !=1")
        public Integer getPendingCount(Long auditee_id);

        @Select("  select count(ismgt.id) from IS_Management_Audit ismgt  "
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ismgt.id "
                        + "AND isma.rectification_status = 0 "
                        + "AND isma.complete_status != 1 "
                        + "AND isma.division_assigned = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id  "
                        + "where isma.auditee_id  = #{auditee_id}")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and
        // complete_status != 1 ) and division_assigned = 1")
        public Integer getOnProgressCount(Long auditee_id);

        @Select("select count(ismgt.id) from IS_Management_Audit ismgt  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ismgt.id "
                        + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        + "AND adi.auditee_submitted = 1 "
                        + //
                        "  where isma.auditee_id  = #{auditee_id}")
        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and
        // complete_status != 1) and response_added = 1 ")
        public Integer getRespondedCount(Long auditee_id);

        @Select("select count(ismgt.id) from IS_Management_Audit ismgt  " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ismgt.id "
                        // + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted = 1 "
                        + //
                        "  where isma.auditee_id  = #{auditee_id}")

        // @Select("select count(id) from IS_Management_Audit where id in (select
        // IS_MGT_id from IS_MGT_Auditee where auditee_id = #{auditee_id} and
        // complete_status = 1) and response_added = 1 and rectification_status = 0")
        public Integer getFinishedCount(Long auditee_id);

        // radar
        @Select("   select count(ima.id) from IS_Management_Audit ima  " + //
                        "   inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1  "
                        + //
                        "   inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id  " + //
                        "   where  isma.auditee_id = #{auditee_id} and DATEDIFF(day, ima.approved_date, getDate()) between #{ range[0]} and #{range[1]} and ima.approve_status = 1")
        public Integer getApprovedFindingsAge(Long auditee_id, int range[]);

        @Select("select count(ima.id) from IS_Management_Audit ima  " + //
                        "   inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 and isma.rectification_status = 0  "
                        + //
                        "   inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id  " + //
                        "   where  isma.auditee_id = #{auditee_id} and DATEDIFF(day, isma.completed_date, getDate()) between #{ range[0]} and #{range[1]}   "
                        + //
                        "")
        public Integer getRespondedFindingsAge(Long auditee_id, int range[]);

        @Select("   select count(ima.id) from IS_Management_Audit ima  " + //
                        "   inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1  "
                        + //
                        "   inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id  " + //
                        "   where  isma.auditee_id = #{auditee_id} and DATEDIFF(day, ima.approved_date, getDate()) > 40 and ima.approve_status = 1")
        public Integer getApprovedFindingsAbove40Dayes(Long auditee_id);

        @Select("select count(ima.id) from IS_Management_Audit ima  " + //
                        "   inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status = 1 and isma.rectification_status = 0  "
                        + //
                        "   inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id  " + //
                        "   where  isma.auditee_id = #{auditee_id} and DATEDIFF(day, isma.completed_date, getDate()) > 40   "
                        + //
                        "")
        public Integer getRespondedFindingsAbove40Dayes(Long auditee_id);

}
