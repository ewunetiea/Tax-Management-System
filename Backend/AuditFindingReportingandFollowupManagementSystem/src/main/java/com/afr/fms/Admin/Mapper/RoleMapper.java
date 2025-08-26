package com.afr.fms.Admin.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.afr.fms.Admin.Entity.Role;
@Mapper
public interface RoleMapper {

	@Select("select * from role")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "jobPositions", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Admin.Mapper.JobPositionMapper.getJobPositionsByRoleIdList"))
	})
	public List<Role> getRoles();

	@Select("select * from role where role_position = #{category}")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public List<Role> getRolesByCategory(String category);

	@Select("select * from role where id = #{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public Role getRoleById(Long id);

	@Select("select * from role where id in (select role_id from role_job_position where job_position_id = #{job_position_id})")
	@Results(value = {
			@Result(property = "id", column = "id"),
	})
	public List<Role> getRoleByPositionID(Long job_position_id);

	@Select("select r.* from role r join user_role ur on r.id = ur.role_id where ur.user_id = #{userId} and r.status=1")
	public List<Role> getRolesByUserId(Long userId);

	@Select("select r.* from role r join user_role ur on r.id = ur.role_id where ur.user_id in (select id from [afrfms].[dbo].[user] where email = #{username}) and r.status=1")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "functionalities", javaType = List.class, column = "id", many = @Many(select = "com.afr.fms.Common.Mapper.FunctionalitiesMapper.getFunctionalitiesByRole"))
	})
	public List<Role> getRolesByUsername(String username);

	@Update("update role set code = #{code}, name= #{name} , description = #{description}, status=#{status}, role_position = #{role_position} where id=#{id}")
	public void updateRole(Role role);

	@Update("update role set status = 1 where id=#{id}")
	public void activate_role(Long id);

	@Update("update role set status = 0 where id=#{id}")
	public void deactivate_role(Long id);

	@Insert("insert into role (code, name, description, role_position, status) values (#{code}, #{name}, #{description}, #{role_position}, 1)")
	public void createRole(Role role);

	@Select("select id from role where code = #{code}")
	public Long getRoleIdByCode(String code);

	@Select("select * from role where code = #{code}")
	public Role getRoleByCode(String code);

}
