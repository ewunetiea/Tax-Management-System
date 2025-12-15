package com.tms.Dashboard.reviewer;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReviewerDashboardMapper {

    // Tax status by branch
    // @Select("SELECT " +
    // "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS Pending, " +
    // "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS Reviewed, " +
    // "SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END) AS Approved, " +
    // "SUM(CASE WHEN status IN (2, 3) THEN 1 ELSE 0 END) AS Rejected " +
    // "FROM tblTaxable " +
    // "WHERE from_ = #{branch_id}")
    // public Map<String, Integer> getTaxStatusCounts(Long branch_id);

    @Select("SELECT " +
            "COALESCE(SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END), 0) AS Pending, " +
            "COALESCE(SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END), 0) AS Reviewed, " +
            "COALESCE(SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END), 0) AS Approved, " +
            "COALESCE(SUM(CASE WHEN status IN (2, 3) THEN 1 ELSE 0 END), 0) AS Rejected " +
            "FROM tblTaxable " +
            "WHERE from_ = #{branch_id}")
    public Map<String, Object> getTaxStatusForReviewer(@Param("branch_id") Long branch_id);

    // Tax status for stacked bar (monthly counts per status)
    @Select("SELECT " +
            "    m.month AS month, " +
            // Pending: status = 0 and maker_date exists in that month
            "    COALESCE(SUM(CASE WHEN t.status = 0 AND MONTH(t.maker_date) = m.month THEN 1 END), 0) AS Pending, " +
            // Reviewed: status = 1 and checked_date exists in that month
            "    COALESCE(SUM(CASE WHEN t.status = 1 AND MONTH(t.checked_date) = m.month THEN 1 END), 0) AS Reviewed, "
            +
            // Approved: status = 5 and approved_date exists in that month
            "    COALESCE(SUM(CASE WHEN t.status = 5 AND MONTH(t.approved_date) = m.month THEN 1 END), 0) AS Approved, "
            +
            // Rejected: status = 2 or 3 and checker_rejected_date exists in that month
            "    COALESCE(SUM(CASE WHEN t.status IN (2, 3) AND MONTH(t.checker_rejected_date) = m.month THEN 1 END), 0) AS Rejected "
            +
            " FROM ( " +
            "    SELECT 1 AS month UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 " +
            "    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 " +
            "    UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 " +
            ") m " +
            " LEFT JOIN tblTaxable t " +
            "   ON t.from_ = #{branch_id} " +
            "GROUP BY m.month ")
    public List<Map<String, Object>> getStackedBarTaxesStatusData(@Param("branch_id") Long branch_id);

}
