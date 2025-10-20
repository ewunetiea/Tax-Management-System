package com.afr.fms.Dashboard.reviewer;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReviewerDashboardMapper {

    // Tax status by branch
    @Select("SELECT " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS Pending, " +
            "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS Reviewed, " +
            "SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END) AS Approved, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS Rejected " +
            "FROM tblTaxable " +
            "WHERE from_ = #{branch_id}")
    public Map<String, Integer> getTaxStatusCounts(Long branch_id);

    // Tax status for stacked bar (monthly counts per status)
    @Select("SELECT " +
            "MONTH(COALESCE(maker_date, checked_date, approved_date, checker_rejected_date)) AS month, " +
            "SUM(CASE WHEN status = 1 AND YEAR(maker_date) = YEAR(CURRENT_TIMESTAMP) THEN 1 ELSE 0 END) AS Pending, " +
            "SUM(CASE WHEN status = 4 AND YEAR(checked_date) = YEAR(CURRENT_TIMESTAMP) THEN 1 ELSE 0 END) AS Reviewed, " +
            "SUM(CASE WHEN status = 5 AND YEAR(approved_date) = YEAR(CURRENT_TIMESTAMP) THEN 1 ELSE 0 END) AS Approved, " +
            "SUM(CASE WHEN status = 2 AND YEAR(checker_rejected_date) = YEAR(CURRENT_TIMESTAMP) THEN 1 ELSE 0 END) AS Rejected " +
            "FROM tblTaxable " +
            "WHERE from_ = #{branch_id} " +
            "GROUP BY MONTH(COALESCE(maker_date, checked_date, approved_date, checker_rejected_date)) " +
            "ORDER BY month")
    public List<Map<String, Object>> getStackedBarTaxesStatusData(@Param("branch_id") Long branch_id);

}
