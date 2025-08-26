
package com.afr.fms.Auditee.DivisionDashboard;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DivisionDashboardMapper {

        // Polar
        @Select("select count(ima.id) from IS_Management_Audit ima " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        // + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted = 1 "
                        + "where adi.division_id = #{division_id}")
        public Integer getRectifiedCount(Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 4 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + " AND adi.auditee_submitted != 1 "
                        + "where adi.division_id = #{division_id}")
        public Integer getPartiallyRectifiedCount(Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 and isma.rectification_status = 4 "
                        + //
                        "inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.auditee_submitted_date is NOT NULL "
                        + //
                        "where adi.division_id = #{division_id}")
        public Integer getTotalPartiallyRectifiedCount(Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 2 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted != 1 "
                        + "where adi.division_id = #{division_id}")
        public Integer getUnrectifiedCount(Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 and isma.rectification_status = 2 "
                        + //
                        "inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.auditee_submitted_date is NOT NULL "
                        + //
                        "where adi.division_id = #{division_id}")
        public Integer getRejectedCount(Long division_id);

        // Stacked bar chart
        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "  inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 " + //
                        "AND isma.rectification_status = 0 "
                        + "  inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.response_submitted != 1  "
                        + //
                        "  where adi.division_id = #{division_id}  and ((SELECT MONTH (adi.division_assigned_date) AS Month) = #{month})")
        public Integer getPendingAuditsCountPerMonth(int month, Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "  inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 " + //
                        "  inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.response_submitted = 1 and adi.submitted_auditee != 1 "
                        + //
                        "  where adi.division_id = #{division_id}  and ((SELECT MONTH (adi.response_subimtted_date) AS Month) =  #{month})")
        public Integer getRespondedAuditsCountPerMonth(int month, Long division_id);

        @Select("  select count(ima.id) from IS_Management_Audit ima " + //
                        "  inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 " + //
                        "  inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.submitted_auditee = 1 and adi.auditee_submitted != 1 "
                        + //
                        "  where adi.division_id = #{division_id}  and ((SELECT MONTH (adi.response_subimtted_date) AS Month) =  #{month})")
        public Integer getFinishedAuditsCountPerMonth(int month, Long division_id);

        // Line Chart
        @Select("select count(ima.id) from IS_Management_Audit ima    " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        // + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted = 1 "
                        + "  where adi.division_id = #{division_id} and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month})")
        public Integer getRectifiedAuditsCountPerMonth(int month, Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima    " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 4 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + " AND adi.auditee_submitted != 1 "
                        + " where adi.division_id = #{division_id} and ( (SELECT MONTH (isma.rectification_date) AS Month) = #{month})")
        public Integer getPartiallyRectifiedAuditsCountPerMonth(int month, Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima    " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 2 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted != 1 "
                        + "    where adi.division_id = #{division_id} and ( (SELECT MONTH (isma.unrectification_date) AS Month) = #{month})")
        public Integer getUnrectifiedAuditsCountPerMonth(int month, Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima    " + //
                        "    inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 and isma.rectification_status = 2    "
                        + //
                        "    inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.auditee_submitted_date is NOT NULL    "
                        + //
                        "    where adi.division_id = #{division_id} and ( (SELECT MONTH (isma.unrectification_date) AS Month) = #{month})")
        public Integer getRejectedAuditsCountPerMonth(int month, Long division_id);

        // Horizontal bar chart
        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "  inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 " + //
                        "  inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id   " + //
                        "  where adi.division_id = #{division_id}  and ((SELECT MONTH (adi.division_assigned_date) AS Month) = #{month})")
        public Integer getTotalPendingAuditsCountPerMonth(int month, Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "  inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 " + //
                        "  inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.response_submitted = 1"
                        + //
                        "  where adi.division_id = #{division_id}  and ((SELECT MONTH (adi.response_subimtted_date) AS Month) =  #{month})")
        public Integer getTotalRespondedAuditsCountPerMonth(int month, Long division_id);

        @Select("  select count(ima.id) from IS_Management_Audit ima " + //
                        "  inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 " + //
                        "  inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.submitted_auditee = 1 "
                        + //
                        "  where adi.division_id = #{division_id}  and ((SELECT MONTH (adi.response_subimtted_date) AS Month) =  #{month})")
        public Integer getTotalFinishedAuditsCountPerMonth(int month, Long division_id);

        // Card
        @Select("select count(ima.id) from IS_Management_Audit ima " + //
                        "inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.division_assigned = 1 " + //
                        "AND isma.rectification_status = 0 "
                        + "inner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and response_submitted != 1 "
                        + //
                        "where adi.division_id = #{division_id}")
        public Integer getPendingCount(Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.rectification_status = 0 "
                        + "AND isma.complete_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        + "AND adi.response_submitted = 1 "
                        + "where adi.division_id = #{division_id}")
        public Integer getOnProgressCount(Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status != 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        + "AND adi.auditee_submitted = 1 "
                        + "where adi.division_id = #{division_id}")
        public Integer getRespondedCount(Long division_id);

        @Select("select count(ima.id) from IS_Management_Audit ima " //
                        + "INNER JOIN IS_MGT_Auditee isma ON isma.IS_MGT_id = ima.id "
                        // + "AND isma.complete_status = 1 "
                        + "AND isma.rectification_status = 1 "
                        + "INNER JOIN Audtiee_Division_ISM adi ON adi.IS_MGT_Auditee_id = isma.id "
                        // + "AND adi.auditee_submitted = 1 "
                        + "where adi.division_id = #{division_id}")
        public Integer getFinishedCount(Long division_id);

        // Radar
        @Select("\tselect count(ima.id) from IS_Management_Audit ima\r\n" + //
                        "\tinner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id \r\n" + //
                        "\tinner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.auditee_submitted != 0\r\n"
                        + //
                        "\twhere  adi.division_id = #{division_id} and DATEDIFF(day, ima.approved_date, getDate()) between #{ range[0]} and #{range[1]} and ima.approve_status = 1\r\n"
                        + //
                        "")
        public Integer getApprovedFindingsAge(Long division_id, int range[]);

        @Select("\tselect count(ima.id) from IS_Management_Audit ima\r\n" + //
                        "\tinner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 0\r\n"
                        + //
                        "\tinner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.auditee_submitted = 1\r\n"
                        + //
                        "\twhere  adi.division_id = #{division_id} and DATEDIFF(day, adi.auditee_submitted_date, getDate()) between #{ range[0]} and #{range[1]}")
        public Integer getRespondedFindingsAge(Long division_id, int range[]);

        @Select("\tselect count(ima.id) from IS_Management_Audit ima\r\n" + //
                        "\tinner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id \r\n" + //
                        "\tinner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.auditee_submitted != 0\r\n"
                        + //
                        "\twhere  adi.division_id = #{division_id} and DATEDIFF(day, ima.approved_date, getDate()) > 40 and ima.approve_status = 1\r\n"
                        + //
                        "")
        public Integer getApprovedFindingsAbove40Dayes(Long division_id);

        @Select("\tselect count(ima.id) from IS_Management_Audit ima\r\n" + //
                        "\tinner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.rectification_status = 0\r\n"
                        + //
                        "\tinner join Audtiee_Division_ISM adi on adi.IS_MGT_Auditee_id = isma.id and adi.auditee_submitted = 1\r\n"
                        + //
                        "\twhere  adi.division_id = #{division_id} and DATEDIFF(day, adi.auditee_submitted_date, getDate()) > 40")
        public Integer getRespondedFindingsAbove40Dayes(Long division_id);

}
