package com.afr.fms.Dashboard.maker;


import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.afr.fms.Admin.Entity.Role;

@Mapper
public interface MakerDashboardMapper {

   
    // Users per role and audit
@Select(
    "SELECT " +
    "COUNT(CASE WHEN status = 6 THEN 1 END) AS drafted, " +
    "COUNT(CASE WHEN status = 0 THEN 1 END) AS waiting, " +
    "COUNT(CASE WHEN status = 2 THEN 1 END) AS reviewed, " +
    "COUNT(CASE WHEN status = 5 THEN 1 END) AS approved " +
    "FROM tblTaxable"
)
public Map<String, Object> fetchPolarDataSingleRow();


    // System users by category
    @Select("SELECT COUNT(id) FROM tblTaxable WHERE status = #{taxtStatus}")
    Integer getCardDataTaxStatus(int taxtStatus);
    


      @Select(
        "SELECT COUNT(*) " +
        "FROM tblTaxable " +
        "WHERE status = #{status} " +
        "AND MONTH( " +
        "    CASE " +
        // "        WHEN #{status} = '6' THEN drafted_date " +
        "        WHEN #{status} = '0' THEN  maker_date " +
        "        WHEN #{status} = '1' THEN checked_Date " +
        "        WHEN #{status} = '5' THEN approved_date " +
        "    END " +
        ") = #{month}"
    )
    int countByStatusAndMonth(@Param("status") int status, @Param("month") int month);

@Select({
    "SELECT ",
    "   MONTH(CASE ",
    "       WHEN status = 0 THEN maker_date ",
    "       WHEN status = 1 THEN checked_Date ",
    "       WHEN status = 5 THEN approved_date ",
    "   END) AS month, ",
    "   status, ",
    "   COUNT(*) AS count ",
    "FROM tblTaxable ",
    "WHERE status IN (0, 1, 5) ",
    "AND YEAR(CASE ",
    "       WHEN status = 0 THEN maker_date ",
    "       WHEN status = 1 THEN checked_Date ",
    "       WHEN status = 5 THEN approved_date ",
    "   END) = YEAR(GETDATE()) ",
    "GROUP BY ",
    "   MONTH(CASE ",
    "       WHEN status = 0 THEN maker_date ",
    "       WHEN status = 1 THEN checked_Date ",
    "       WHEN status = 5 THEN approved_date ",
    "   END), ",
    "   status"
})
List<BarChartRow> countAllByStatusAndMonthGrouped();


  



    @Select(
    "SELECT " +
    "   SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS drafted, " +
    "   SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS waitingForReview, " +
    "   SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS reviewed, " +
    "   SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) AS reviewerRejected, " +
    "   SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END) AS approved, " +
    "   SUM(CASE WHEN status = 6 THEN 1 ELSE 0 END) AS approverRejected " +
    "FROM tblTaxable"
)
public RadarPayload getStatusCounts();



  @Select(
    "SELECT " +
    "   YEAR(CASE " +
    "       WHEN status = 6 THEN drafted_date " +
    "       WHEN status = 0 THEN maker_date " +
    "       WHEN status = 1 THEN checked_Date " +
    "       WHEN status = 2 THEN checker_rejected_date " +
    "       WHEN status = 5 THEN approved_date " +
    "       WHEN status = 4 THEN approver_rejected_date " +
    "   END) AS year, " +
    "   SUM(CASE WHEN status = 6 THEN 1 ELSE 0 END) AS drafted, " +
    "   SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS waitingForReview, " +
    "   SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS reviewed, " +
    "   SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS reviewerRejected, " +
    "   SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END) AS approved, " +
    "   SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS approverRejected " +
    "FROM tblTaxable " +
    "WHERE YEAR(CASE " +
    "       WHEN status = 6 THEN drafted_date " +
    "       WHEN status = 0 THEN maker_date " +
    "       WHEN status = 1 THEN checked_Date " +
    "       WHEN status = 2 THEN checker_rejected_date " +
    "       WHEN status = 5 THEN approved_date " +
    "       WHEN status = 4 THEN approver_rejected_date " +
    "   END) IN (YEAR(GETDATE()), YEAR(GETDATE()) - 1) " +
    "GROUP BY YEAR(CASE " +
    "       WHEN status = 6 THEN drafted_date " +
    "       WHEN status = 0 THEN maker_date " +
    "       WHEN status = 1 THEN checked_Date " +
    "       WHEN status = 2 THEN checker_rejected_date " +
    "       WHEN status = 5 THEN approved_date " +
    "       WHEN status = 4 THEN approver_rejected_date " +
    "   END) " +
    "ORDER BY year DESC"
)
List<RadarPayload> getStatusCountsForCurrentAndPreviousYear();

}





