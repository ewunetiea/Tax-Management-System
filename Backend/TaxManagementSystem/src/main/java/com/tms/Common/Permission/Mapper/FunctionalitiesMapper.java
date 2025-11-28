package com.tms.Common.Permission.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.tms.Admin.Entity.Role;
import com.tms.Common.Entity.Functionalities;

@Mapper
public interface FunctionalitiesMapper {

        @Select("select * from functionality ORDER BY id DESC")
        @Results(value = { @Result(property = "id", column = "id") })
        public List<Functionalities> getAllFunctionalities();

        @Select("select f.id, f.name, f.description, f.category, f.method, rf.status from functionality f "
                        + " inner join role_functionality rf on rf.functionality_id = f.id and rf.role_id = #{role_id} where f.status = 1 ")
        @Results(value = {
                        @Result(property = "id", column = "id")
        })
        public List<Functionalities> getAllFunctionalitiesByRole(Long role_id);

        @Select("select f.id, f.name, f.description, mp.status from functionality f "
                        + " inner join menu_permission mp on mp.permission_id = f.id and mp.item_id = #{item_id} where f.status = 1 ")
        @Results(value = {
                        @Result(property = "id", column = "id")
        })
        public List<Functionalities> getAllFunctionalitiesByMenuItem(Long item_id);

        @Insert("insert into functionality(name,description,category, method ,status) values(#{name},#{description},#{category}, #{method}, 1)")
        public void createFunctionality(Functionalities functionality);

        @Select("SELECT COUNT(*) FROM functionality WHERE name = #{name} AND method = #{method}")
        public int countByNameAndMethod(@Param("name") String name, @Param("method") String method);

        @Update("update functionality set name = #{name}, description = #{description}, category=#{category}, method = #{ method} where id = #{id}")
        public void updateFunctionality(Functionalities functionalities);

        @Update("update functionality set status = 0 where id = #{id}")
        public void deactivatePermission(Long id);

        @Update("delete from functionality where id = #{id}")
        public void deletePermission(Long id);

        @Update("update functionality set status = 1 where id = #{id}")
        public void activatePermission(Long id);

        @Update("update role_functionality set status = #{status} where role_id = #{role_id} and functionality_id = #{functionality_id}")
        public void updateRoleFunctionalitiesById(@Param("role_id") Long role_id,
                        @Param("functionality_id") Long functionality_id, @Param("status") boolean status);

        @Insert("insert into role_functionality (role_id, functionality_id, status) values (#{role_id}, #{functionalities_id}, #{status} )")
        public void assignPermission(Long role_id, Long functionalities_id, boolean status);

        @Select("<script>" +
                        "WITH role_assigned_funs AS ( " +
                        "    SELECT f.id, rf.status " +
                        "    FROM functionality f " +
                        "    INNER JOIN role_functionality rf " +
                        "        ON rf.functionality_id = f.id " +
                        "        AND rf.role_id = #{id} " +
                        ") " +
                        "SELECT " +
                        "    f.id, " +
                        "    f.name, " +
                        "    f.category, " +
                        "    f.method, " +
                        "    f.description, " +
                        "    CASE " +
                        "        WHEN rf.id IS NOT NULL THEN rf.status " +
                        "        ELSE f.status " +
                        "    END AS status " +
                        "FROM functionality f " +
                        "LEFT JOIN role_assigned_funs rf " +
                        "    ON rf.id = f.id " +
                        "WHERE ( " +
                        "    <if test='role_position == \"GENERAL\"'> " +
                        "        f.category = #{code} " +
                        "    </if> " +
                        "    <if test='role_position != \"GENERAL\"'> " +
                        "        (f.category COLLATE Latin1_General_CS_AS = 'ALL_' + #{role_position} " +
                        "         OR f.category COLLATE Latin1_General_CS_AS LIKE '%' + #{code} + '%') " +
                        "    </if> " +
                        "    OR f.category = 'ALL' " +
                        ")" +
                        "</script>")
        public List<Functionalities> getFunctionalityByCategory(Role role);

        @Delete("delete from role_functionality where role_id = #{role_id} ")
        public void deleteRoleFunctionality(Long role_id);

        @Select("SELECT DISTINCT f.id, f.name, f.description, rf.status  FROM functionality f "
                        + "INNER JOIN role_functionality rf ON rf.functionality_id = f.id AND rf.role_id = #{role_id} ")

        @Results(value = {
                        @Result(property = "id", column = "id")
        })
        public List<Functionalities> getFunctionalitiesByRole(Long role_id);

        @Select({
                        "<script>",
                        "SELECT CASE",
                        "    WHEN EXISTS (",
                        "        SELECT 1",
                        "        FROM functionality f",
                        "        INNER JOIN role_functionality rf ON rf.functionality_id = f.id AND rf.role_id = #{role_id}",
                        "        LEFT JOIN user_functionality uf ON uf.role_functionality_id = rf.id",
                        "            AND uf.user_id = (",
                        "                SELECT id FROM [user] WHERE username = #{username} OR email = #{username}",
                        "            )",
                        "        WHERE f.status = 1",
                        "          AND rf.status = 1",
                        "          AND f.method = #{method}",
                        "          AND uf.role_functionality_id IS NULL",
                        "          AND f.name LIKE CONCAT('%', #{functionality_name}, '%')",
                        "    ) THEN 1",
                        "    ELSE 0",
                        "END",
                        "</script>"
        })
        boolean checkFunctionalityExists(
                        @Param("role_id") Long roleId,
                        @Param("username") String username,
                        @Param("functionality_name") String functionalityName,
                        @Param("method") String method);

        @Select("select permission_id from menu_permission where item_id IN ( #{items} ) ")
        public List<Long> getPermissionsByMenuItemID(String items);

        @Select("SELECT CAST(value AS BIGINT) AS id " +
                        "FROM STRING_SPLIT(#{permissions}, ',') " +
                        "WHERE CAST(value AS BIGINT) NOT IN (" +
                        "    SELECT functionality_id " +
                        "    FROM role_functionality " +
                        "    WHERE role_id = #{role_id}" +
                        ")")
        public List<Long> getUnassignedPermissionsFromRole(@Param("role_id") Long role_id,
                        @Param("permissions") String permissions);

        @Delete("WITH assigned_permissions AS (" +
                        "    SELECT mp.permission_id " +
                        "    FROM menu_role mr " +
                        "    INNER JOIN menu_items mi ON mi.header_id = mr.header_id " +
                        "    INNER JOIN menu_permission mp ON mp.item_id = mi.id " +
                        "    WHERE mr.header_id != #{header_id} " +
                        "    AND mr.role_id = #{role_id} " +
                        ") " +
                        "DELETE FROM role_functionality " +
                        "WHERE functionality_id IN ( #{permissions} ) " +
                        "AND role_id = #{role_id} " +
                        "AND functionality_id NOT IN (SELECT permission_id FROM assigned_permissions)")
        void deleteUnassignedPermissionsRoleID(
                        @Param("role_id") Long role_id,
                        @Param("header_id") Long header_id,
                        @Param("permissions") String permissions);

}
