package com.tms.Dashboard.approver;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApproverDashboardMapper {


     @Select("SELECT " +
            "COALESCE(SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END), 0) AS Pending, " +
            "COALESCE(SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END), 0) AS Approved, " +
            "COALESCE(SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END), 0) AS Rejected " +
            "FROM tblTaxable " 
            // "WHERE sendTo_ = #{branch_id}"
        
        )
    public Map<String, Object> getTaxStatusForApprover(@Param("branch_id") Long branch_id);



// @Select(
//     "SELECT " +
//     "    m.month AS month, " +

//     "    COALESCE(SUM(CASE " +
//     "        WHEN t.status = 1 " +
//     // "         AND t.sendTo_ = #{branch_id} " +
//     "         AND t.checked_date IS NOT NULL " +
//     "         AND MONTH(t.checked_date) = m.month " +
//     "        THEN 1 ELSE 0 END), 0) AS Pending, " +

//     "    COALESCE(SUM(CASE " +
//     "        WHEN t.status = 5 " +
//     // "         AND t.sendTo_ = #{branch_id} " +
//     "         AND t.approved_date IS NOT NULL " +
//     "         AND MONTH(t.approved_date) = m.month " +
//     "        THEN 1 ELSE 0 END), 0) AS Approved, " +

//     "    COALESCE(SUM(CASE " +
//     "        WHEN t.status = 3 " +
//     // "         AND t.sendTo_ = #{branch_id} " +
//     "         AND t.approver_rejected_date IS NOT NULL " +
//     "         AND MONTH(t.approver_rejected_date) = m.month " +
//     "        THEN 1 ELSE 0 END), 0) AS Rejected " +

//     "FROM ( " +
//     "    SELECT 1 AS month UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 " +
//     "    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 " +
//     "    UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 " +
//     ") m " +
//     "LEFT JOIN tblTaxable t ON 1 = 1 " +
//     "GROUP BY m.month " +
//     "ORDER BY m.month"
// )
// public List<Map<String, Object>> getStackedBarTaxesStatusDataForApprover(
//         @Param("branch_id") Long branch_id
// );

@Select(
    "SELECT " +
    "   MONTH(CASE " +
    "       WHEN status = 1 THEN checked_date " +
    "       WHEN status = 5 THEN approved_date " +
    "       WHEN status = 3 THEN approver_rejected_date " +
    "   END) AS month, " +
    "   status, " +
    "   COUNT(*) AS count " +
    "FROM tblTaxable " +
    "WHERE status IN (1, 3, 5) " +
    "  AND ( " +
    "       (status = 1 AND checked_date IS NOT NULL) OR " +
    "       (status = 5 AND approved_date IS NOT NULL) OR " +
    "       (status = 3 AND approver_rejected_date IS NOT NULL) " +
    "  ) " +
    "GROUP BY " +
    "   MONTH(CASE " +
    "       WHEN status = 1 THEN checked_date " +
    "       WHEN status = 5 THEN approved_date " +
    "       WHEN status = 3 THEN approver_rejected_date " +
    "   END), status " +
    "ORDER BY month, status"
)
List<Map<String, Object>> getStackedBarTaxesStatusDataForApprover(
        @Param("branch_id") Long branch_id
);



}
