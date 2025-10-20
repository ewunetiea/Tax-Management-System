package com.afr.fms.Dashboard.maker;


import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.afr.fms.Admin.Entity.Role;

@Mapper
public interface MakerDashboardMapper {

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

    // System users by category
    @Select("SELECT COUNT(id) FROM tblTaxable WHERE status = #{taxtStatus}")
    Integer getTaxStatus(int taxtStatus);
    

    // User login status
    @Select("SELECT COUNT(id) FROM user_tracker WHERE status = #{status}")
    Integer getUserLoginStatus(int status);

    // ✅ Optimized Polar Data (all counts in one query)
    @Select("SELECT " +
            "    SUM(CASE WHEN u.status = 1 THEN 1 ELSE 0 END) AS active_users, " +
            "    SUM(CASE WHEN u.status = 0 THEN 1 ELSE 0 END) AS inactive_users, " +
            "    SUM(CASE WHEN us.accountNonLocked = 0 THEN 1 ELSE 0 END) AS account_locked_users, " +
            "    SUM(CASE WHEN us.credentialsNonExpired = 0 THEN 1 ELSE 0 END) AS credential_expired_users " +
            "FROM [user] u " +
            "LEFT JOIN user_security us ON u.id = us.user_id")
    Map<String, Integer> getPolarDataCounts();

    // User active status per audit
    @Select("SELECT COUNT(id) FROM [user] WHERE status = #{status} AND category = #{category}")
    Integer getUserActiveStatusPerAudit(int status, String category);

    // Audit sample
    @Select("SELECT COUNT(id) " +
            "FROM IS_Management_Audit " +
            "WHERE auditor_id = #{auditor_id} AND auditor_status = 1 AND review_status = 0")
    Integer getPassedCount(Long auditor_id);
}
