
package com.tms.Dashboard.admin;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.tms.Admin.Entity.Role;

@Mapper
public interface AdminDashboardMapper {

    // Roles by type
    @Select("SELECT * FROM role WHERE role_position = #{role_position} ORDER BY code")
    List<Role> getRolesByAuditType(String role_position);

    // Users per role and audit
    @Select("SELECT COUNT(id) " +
            "FROM [user] " +
            "WHERE category = #{category} " +
            "AND id IN ( " +
            "    SELECT user_id " +
            "    FROM user_role " +
            "    WHERE role_id IN ( " +
            "        SELECT id " +
            "        FROM role " +
            "        WHERE code LIKE CONCAT('%', #{code}, '%') " +
            "          AND role_position = #{category} " +
            "    ) " +
            ")")
    Integer getUsersPerRoleandAudit(String category, String code);

    // Users per region
    @Select("SELECT COUNT(id) " +
            "FROM [user] u " +
            "WHERE u.category = #{category} " +
            "  AND (u.branch_id IN ( " +
            "           SELECT br.id FROM branch br WHERE br.region_id = #{region_id} " +
            "      ) OR u.region_id = #{region_id})")
    Integer getUsersPerRegion(String category, Long region_id);

      @Select(
        "SELECT COUNT(us.id) AS user_count " +
        "FROM [user] us " +
        "INNER JOIN user_role ur ON ur.user_id = us.id " +
        "INNER JOIN role r ON r.id = ur.role_id " +
        "WHERE r.id IN (1, 2, 3, 4) " +
        "GROUP BY r.id " +
        "ORDER BY r.id"
    )
    List<Integer> computeCardData();

    // User login status
    @Select("SELECT COUNT(id) FROM user_tracker WHERE status = #{status}")
    Integer getUserLoginStatus(int status);

   

      @Select(
        "SELECT " +
        "   SUM(CASE WHEN u.status = 1 THEN 1 ELSE 0 END) AS active_users, " +
        "   SUM(CASE WHEN u.status = 0 THEN 1 ELSE 0 END) AS inactive_users " +
      
        "FROM [user] u " 
    )
  public  Map<String, Integer> getDougnutDataCounts();


   
      @Select(
        "SELECT " +
        "   r.name AS region_name, " +
        "   COUNT(b.id) AS branch_count " +
        "FROM region r " +
        "LEFT JOIN branch b ON b.region_id = r.id " +
        "GROUP BY r.name " +
        "ORDER BY r.name"
    )
    List<Map<String, Object>> getBranchCountPerRegion();
 
}
