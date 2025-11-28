package com.tms.Common.Permission.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.tms.Admin.Entity.User;
import com.tms.Common.Entity.Functionalities;

@Mapper
public interface UserFunctionalityMapper {

        @Select("<script> " +
                        " select * from [user] " +
                        "  WHERE 1=1 " +
                        "  <if test=\"branchId != null\">  " +
                        "    AND branch_id = #{branchId}  " +
                        "  </if>  " +
                        "  <if test=\"regionId != null\">  " +
                        "    AND region_id = #{regionId}   " +
                        "  </if>  " +
                        "  <if test=\"positionId != null\">  " +
                        "    AND job_position_id = #{positionId}  " +
                        "  </if>  " +
                        "  <if test=\"first_name != null\">  " +
                        "    AND  first_name LIKE '%' + #{first_name} + '%' " +
                        "  </if>  " +
                        "  <if test=\"middle_name != null\">  " +
                        "    AND  middle_name LIKE '%' + #{middle_name} + '%' " +
                        "  </if>  " +
                        "  <if test=\"last_name != null\">  " +
                        "    AND  last_name LIKE '%' + #{last_name} + '%' " +
                        "  </if>  " +
                        "  <if test=\"email != null\">  " +
                        "    AND  email LIKE '%' + #{email} + '%' " +
                        "  </if>  " +

                        "  <if test=\"gender != null\">  " +
                        "    AND  gender LIKE '%' + #{gender} + '%' " +
                        "  </if>  " +

                      

                        "  <if test=\"employee_id != null\">  " +
                        "    AND  emp_id LIKE '%' + #{employee_id} + '%' " +
                        "  </if>  " +

                        "  <if test=\"phone_number != null\">  " +
                        "    AND  phone_number LIKE '%' + #{phone_number} + '%' " +
                        "  </if>  " +

                        "</script>  " +
                        "")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "functionalities", javaType = List.class, column = "id", many = @Many(select = "com.tms.Common.Permission.Mapper.UserFunctionalityMapper.getUserFunctionalitiesById"))
        })
        List<User> generatedUsers(
                        @Param("first_name") String first_name,
                        @Param("middle_name") String middle_name,
                        @Param("last_name") String last_name,
                        @Param("email") String email,
                        @Param("phone_number") String phone_number,
                        @Param("branchId") Long branchId,
                        @Param("regionId") Long regionId,
                        @Param("positionId") Long positionId,
                        @Param("gender") String gender,
                        @Param("employee_id") String employee_id);

        @Select("SELECT   " +
                        "CASE   " +
                        "WHEN uf.id is not null THEN uf.id   " +
                        "ELSE rf.id   " +
                        "END AS id, f.name, f.description, f.method, f.category,   " +
                        "CASE   " +
                        "WHEN uf.role_functionality_id is not null THEN 0   " +
                        "ELSE 1   " +
                        "END AS status   " +
                        "FROM functionality f   " +
                        "INNER JOIN role_functionality rf ON f.id = rf.functionality_id   " +
                        "inner join user_role ur on ur.role_id = rf.role_id   " +
                        "inner join [user] u on u.id = ur.user_id   " +
                        "left join user_functionality uf on uf.role_functionality_id = rf.id and u.id = uf.user_id   "
                        +
                        "WHERE u.id = #{id}")

        public List<Functionalities> getUserFunctionalitiesById(Long id);

        @Insert("insert into user_functionality (user_id,role_functionality_id,status) values (#{user_id}, #{role_functionality_id}, #{status})")
        public void deactivateUserFunctionality(Long user_id, Long role_functionality_id, boolean status);

        @Delete("delete from user_functionality where id = #{user_functionality_id} ")
        public void deleteUserFunctionalityBYId(Long user_functionality_id);

}
