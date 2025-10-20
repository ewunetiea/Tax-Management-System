package com.afr.fms.Admin.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.afr.fms.Admin.Entity.JobPosition;
import com.afr.fms.Admin.Entity.JobPositionRole;
import com.afr.fms.Admin.Entity.Role;

@Mapper
public interface JobPositionMapper {

        @Select("Select * from role_job_position where role_id = #{id}")
        @Results(value = {
                        @Result(property = "role", column = "role_id", one = @One(select = "com.bfp.elfms.Admin.Mapper.UserRoleMapper.getRoleById")),
                        @Result(property = "jobPositions", column = "job_position_id", one = @One(select = "com.bfp.elfms.Admin.Mapper.JobPositionMapper.getJobPositionById"))
        })
        public JobPositionRole getJobPositionsByRole(Role role);

        @Insert("INSERT INTO role_job_position (role_id, job_position_id) VALUES (#{role.id}, #{jobPosition.id})")
        public void addJobPositionsByRole(Role role, JobPosition jobPosition);

        @Insert("INSERT INTO job_position (title, location) VALUES (#{title}, #{location})")
        public void addJobPosition(JobPosition jobPosition);

        @Delete("DELETE FROM role_job_position where role_id = #{role.id}")
        public void deleteJobPositionsByRole(JobPositionRole jobPositionRole);

        // @Select("Select * from role_job_position where role_id = #{id}")
        // @Results(
        // value =
        // {
        // // @Result(property = "role", column = "role_id", one = @One(select =
        // "com.bfp.elfms.Admin.Mapper.UserRoleMapper.getRoleById")),
        // @Result(property = "jobPositions", column = "job_position_id", one =
        // @One(select =
        // "com.bfp.elfms.Admin.Mapper.JobPositionMapper.getJobPositionById"))
        // }
        // )
        // public JobPositionRole getJobPositionsByRoleId(Role role);

        @Select("Select * from job_position where id in (Select job_position_id from role_job_position where role_id = #{role_id})")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<JobPosition> getJobPositionsByRoleIdList(Long role_id);

        // @Select("Select * from role where id in (Select distinct(role_id) from role_job_position where job_position_id = #{job_position_id}) and role_position = #{role_position}")   this was previewes user by category specially  ins, bfa, is, mgt, admin
                @Select("Select * from role where id in (Select distinct(role_id) from role_job_position where job_position_id = #{job_position_id}) ")

        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<Role> getRoleByJobPositionId(Long job_position_id, String role_position);

        @Select("Select * from role where id in (Select distinct(role_id) from role_job_position where job_position_id = #{job_position_id})")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<Role> getRoleByJobPositionIdScheduler(Long job_position_id);

        @Select("Select * from job_position where id = #{id}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public JobPosition getJobPositionById(Long id);

        @Select("Select * from job_position where title = #{title}")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public JobPosition getJobPositionByTitle(String title);

        @Select("SELECT * FROM job_position WHERE LTRIM(RTRIM(title)) = LTRIM(RTRIM(#{title}))")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<JobPosition> getJobPositionsByTitle(String title);

        @Select("Select * from job_position where id in (SELECT DISTINCT(job_position_id) from role_job_position) ORDER BY title")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<JobPosition> getJobPositions();

        @Select("Select * from job_position where id not in (SELECT DISTINCT(job_position_id) from role_job_position) ORDER BY title")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<JobPosition> getAllJobPositions();

        @Select("Select * from job_position ORDER BY title")
        @Results(value = {
                        @Result(property = "id", column = "id"),
        })
        public List<JobPosition> getTotalJobPositions();

        @Select("Select title from job_position ORDER BY title")
        public List<String> getAllJobTitles();

        @Select("select count(id) from role_job_position where job_position_id = #{id}")
        public Long checkJobPositionRole(Long id);
        // checkJobPositionRole

        @Select("Select * from job_position where id in (SELECT DISTINCT(job_position_id) from role_job_position) ORDER BY title")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "roles", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.RoleMapper.getRoleByPositionID")),
        })
        public List<JobPosition> getMappedJobPositions();

}
