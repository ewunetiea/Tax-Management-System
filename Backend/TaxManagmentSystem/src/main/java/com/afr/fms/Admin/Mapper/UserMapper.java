package com.afr.fms.Admin.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.*;

import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Auditor.Entity.AuditISM;

@Mapper
public interface UserMapper {

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

                        "  <if test=\"category != null\">  " +
                        "    AND  category = #{category}  " +
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
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "userCopyFromHR", column = "emp_id", one = @One(select = "com.afr.fms.Admin.Mapper.CopyHRUsersMapper.checkUserEmployeeId")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.UserRoleMapper.getRolesByUserId"))
        })
        List<User> generatedUsers(
                        @Param("first_name") String first_name,
                        @Param("middle_name") String middle_name,
                        @Param("last_name") String last_name,
                        @Param("email") String email,
                        @Param("phone_number") String phone_number,
                        @Param("category") String category,
                        @Param("branchId") Long branchId,
                        @Param("regionId") Long regionId,
                        @Param("positionId") Long positionId,
                        @Param("gender") String gender,
                        @Param("employee_id") String employee_id);

        @Select("select * from [user]")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "userCopyFromHR", column = "emp_id", one = @One(select = "com.afr.fms.Admin.Mapper.CopyHRUsersMapper.checkUserEmployeeId")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.UserRoleMapper.getRolesByUserId"))
        })
        public List<User> getUsers();

        @Select("select * from [user]")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.UserRoleMapper.getRolesByUserId"))
        })
        public List<User> getUsersStatus();

        @Select("SELECT * FROM [user] WHERE id= #{id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "userCopyFromHR", column = "emp_id", one = @One(select = "com.afr.fms.Admin.Mapper.CopyHRUsersMapper.checkUserEmployeeId"))

        })
        public User getUserById(Long id);

        @Select("SELECT * FROM [user] WHERE id= #{id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),

                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
        })
        public User getAuditorById(Long id);

        // @Select("SELECT * FROM [user] WHERE first_name like
        // '%'+#{key}+'%' "
        // + "or last_name like '%'+#{key}+'%' "
        // + "or email like '%'+#{key}+'%' "
        // + "or phone_number like '%'+#{key}+'%' ")
        public List<User> searchUser(String key);

        @Select("select * from [user] where username = #{username}")
        public User getUserByUsername(String username);

        @Select("select * from [user] where RIGHT(phone_number, 9) = RIGHT(#{phone_number}, 9)")
        public User getUserIdByPhoneNumber(String phone_number);

        @Select("INSERT INTO [user](username, first_name, middle_name, last_name, email, password, phone_number, status, branch_id, photo_url, region_id, gender, emp_id, job_position_id, category, banking, special_user)"
                        + "  OUTPUT inserted.id VALUES (#{username}, #{first_name},#{middle_name}, #{last_name}, #{email}, #{password}, #{phone_number},0, #{branch.id}, #{photoUrl}, #{region.id}, #{gender}, #{employee_id}, #{jobPosition.id}, #{category}, #{banking}, #{special_user})")
        public Long create_user(User user);

        @Update("UPDATE [user] "
                        + " SET first_name = #{first_name}, "
                        + " last_name = #{last_name}, "
                        + " middle_name = #{middle_name}, "
                        + " username = #{username}, "
                        + " email = #{email}, "
                        + " phone_number = #{phone_number}, "
                        + " status = #{status}, "
                        + " branch_id = #{branch.id}, "
                        + " region_id = #{region.id}, "
                        + " emp_id = #{employee_id} ,"
                        + " photo_url = #{photoUrl} ,"
                        + " gender = #{gender},"
                        + " job_position_id = #{jobPosition.id},"
                        + " category = #{category}, "
                        + " special_user = #{special_user}, "
                        + " banking = #{banking}"
                        + " WHERE id = #{id}")
        public void updateUser(User user);

        @Update("UPDATE [user] set"
                        + " status = #{status}, "
                        + " branch_id = #{branch.id}, "
                        + " region_id = #{region.id}, "
                        + " job_position_id = #{jobPosition.id}"
                        + " WHERE id = #{id}")
        public void updateUserScheduler(User user);

        @Update("UPDATE [user] SET status=#{status} WHERE id=#{id}")
        public void changeUserStatus(Long id, Boolean status);

        @Update("UPDATE [user] SET branch_id = #{branch.id} WHERE id=#{id}")
        public void transferBranch(User user);

        @Update("UPDATE [user] SET region_id = #{region.id} WHERE id=#{id}")
        public void transferRegion(User user);

        @Update("UPDATE [user] SET special_user = #{special_user} WHERE id=#{id}")
        public void makeSpecialUser(User user);

        @Update("UPDATE [user] SET region_id = #{region.id}, branch_id = #{branch.id} WHERE id=#{id}")
        public void transferUser(User user);

        @Update("UPDATE [user] "
                        + "SET status = 1 WHERE id=#{id}")
        public void accountVerified(Long id);

        @Select("select id from [user] where email = #{email} or username = #{email}")
        public long getUserIdByEmail(String email);

        @Select("select email from [user] where id = #{id}")
        public long getEmailById(Long id);

        @Select("select * from [user] where id = #{id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
        })
        public User findById(Long id);

        @Select("select * from [user] where email = #{email} or username = #{email}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "userCopyFromHR", column = "emp_id", one = @One(select = "com.afr.fms.Admin.Mapper.CopyHRUsersMapper.checkUserEmployeeId")),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId"))
        })
        public User findByUsername(String email);

        @Select("select * from [user] where emp_id = #{emp_id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "userCopyFromHR", column = "emp_id", one = @One(select = "com.afr.fms.Admin.Mapper.CopyHRuserMapper.checkUserEmployeeId")),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.UserRoleMapper.getRolesByUserId"))
        })
        public User findByEmployeeID(String emp_id);

        @Select("select top 1 * from [user] where emp_id = #{emp_id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.UserRoleMapper.getRolesByUserId"))
        })
        public User findByEmployeeIDScheduler(String emp_id);

        @Select("select * from [user] where (email = #{email} or username = #{email}) and status = 1")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.UserRoleMapper.getRolesByUserId"))
        })
        public User findByVerifiedEmail(String email);

        @Select("select * from [user] where phone_number = #{phone_number} and status = 1")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.UserRoleMapper.getRolesByUserId"))
        })
        public User findByVerifiedPhoneNumber(String phone_number);

        @Select("Select photo_url from [user] where id=#{id}")
        public String getPhotoUrlById(Long id);

        @Select("select count(u.id) as numberOfMakers from [user] u "
                        + "inner join user_role ur on ur.user_id = u.id and ur.role_id in (select id from role where code = 'AUDITOR')"
                        + " where u.branch_id in (select br.id from branch br where br.region_id=#{region_id}) or u.region_id =#{region_id}")
        public Long getMakersPerRegion(Long region_id);

        @Select("select count(u.id) as numberOfApprovers from [user] u " +
                        "inner join user_role ur on ur.user_id = u.id and ur.role_id in (select id from role where code = 'APPROVER')"
                        + " where u.region_id=#{region_id}")
        public Long getApproversPerRegion(Long region_id);

        @Select("select * from [user] where email = #{email} or username = #{email}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public User findByEmail(String email);

        @Select("select * from [user] where branch_id = #{branch_id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
        })
        public List<User> findUserByBranchID(Long branch_id);

        @Select("SELECT * FROM [user] WHERE branch_id = #{branch_id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
        })
        public List<User> getUserByDirectorateID(Long branch_id);

        // checkEmployeeIdSystem
        @Select("select * from [user] where emp_id = #{employee_id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public User checkEmployeeIdSystem(String employee_id);

        @Select("select * from [user] where job_position_id = #{job_position_id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public List<User> getUsersByJobPositionId(Long job_position_id);

        @Select("select * from [user] where job_position_id = #{job_position_id} and category = #{role_location} and special_user != 1")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),

        })
        public List<User> getUsersByJobPositionIdandRolePosition(Long job_position_id, String role_location);

        @Select("Select * from [user] where id in (SELECT user_id from user_role where role_id = #{role_id})")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public List<User> getUsersByRole(Long role_id);

        @Select("Select * from [user] where id in (SELECT user_id from user_role where role_id in (select id from role where code = #{code})) and category= #{category}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public User getUserByRole(String code, String category);

        @Delete("delete from [user] where id = #{user_id}")
        public void deleteUserById(Long user_id);

        @Select("SELECT * FROM [user] WHERE category= #{category}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public List<User> getUserByCategory(String category);

        @Select("select * from [user] u  " + //
                        "inner join IS_Management_Audit isma1 on (isma1.auditor_id = u.id  " + //
                        "or isma1.reviewer_id = u.id  " + //
                        "or isma1.approver_id = u.id  " + //
                        "or isma1.followup_id = u.id) and isma1.id = #{id}  " + //
                        "where u.category = #{category}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public List<User> getUsersRemark(AuditISM auditISM);

        @Select("SELECT * FROM [user] WHERE id in (select user_id from user_role where role_id in ( select id from role where code = #{role} )) and category= #{category} ")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public List<User> getUserByCategoryandRole(String category, String role);

        @Select("SELECT id, email FROM [user] WHERE id in (select user_id from user_role where role_id in ( select id from role where name = #{role} )) ")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<User> getUserByRoleName(String role);

        @Select("SELECT * FROM [user] WHERE id in (select user_id from user_role where role_id in ( select id from role where code = #{role} )) and banking= #{banking} ")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public List<User> getUserByBankingandRole(String banking, String role);

        @Select("SELECT * FROM [user] WHERE id in (select user_id from user_role where role_id in ( select id from role where code = #{role} )) and branch_id = #{branch_id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),
        })
        public List<User> getUserByBranchandRole(Long branch_id, String role);

        @Insert("insert into user_role (user_id, role_id) values (#{id}, #{role.id})")
        public void addUserRole(Long id, Role role);

        @Delete("delete from user_role where user_id = #{id}")
        public void removeAllUserRoles(Long id);

        @Delete("delete from user_role where user_id = #{user_id} and role_id = #{role_id}")
        public void removeRolesByUserRole(Long user_id, Long role_id);

        @Select("select * from [user] where email = #{username} or username = #{username}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "employee_id", column = "emp_id"),
                        @Result(property = "userCopyFromHR", column = "emp_id", one = @One(select = "com.afr.fms.Admin.Mapper.CopyHRUsersMapper.checkUserEmployeeId")),
                        @Result(property = "branch", column = "branch_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
                        @Result(property = "region", column = "region_id", one = @One(select = "com.afr.fms.Admin.Mapper.RegionMapper.getRegionById")),
                        @Result(property = "photoUrl", column = "photo_url"),
                        @Result(property = "user_security", column = "id", one = @One(select = "com.afr.fms.Security.UserSecurity.mapper.UserSecurityMapper.getUserSecurityInfoByUserId")),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRolesByUserId")),
                        @Result(property = "jobPosition", column = "job_position_id", one = @One(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionById")),

        })
        public User findByFusionUsername(String username);

}
