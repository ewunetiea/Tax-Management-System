package com.afr.fms.Dashboard.approver;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApproverDashboardMapper {

    // Tax status by branch
    @Select("SELECT " +
            "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS Pending, " +
            "SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END) AS Approved, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS Rejected " +
            "FROM tblTaxable " +
            "WHERE sendTo_ = #{branch_id}")
    public Map<String, Integer> getTaxStatusForApprover(Long branch_id);

    // Tax status for stacked bar (monthly counts per status)
    @Select("SELECT " +
            "    m.month AS month, " +
            "    COALESCE(SUM(CASE WHEN t.status =4 THEN 1 END), 0) AS Pending, " +
            "    COALESCE(SUM(CASE WHEN t.status = 5 THEN 1 END), 0) AS Approved, " +
            "    COALESCE(SUM(CASE WHEN t.status = 2 THEN 1 END), 0) AS Rejected " +
            "FROM ( " +
            "    SELECT 1 AS month UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 " +
            "    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 " +
            "    UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 " +
            ") m " +
            "LEFT JOIN tblTaxable t " +
            "    ON MONTH(COALESCE(t.checked_date, t.approved_date, t.approver_rejected_date)) = m.month " +
            "    AND t.sendTo_ = #{branch_id} " + 
            "    AND YEAR(COALESCE(t.checked_date, t.approved_date, t.approver_rejected_date)) = YEAR(CURRENT_TIMESTAMP) "
            +
            "GROUP BY m.month " +
            "ORDER BY m.month")
    public List<Map<String, Object>> getStackedBarTaxesStatusDataForApprover(@Param("branch_id") Long branch_id);

}
