package com.afr.fms.Admin.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import com.afr.fms.Admin.Entity.Role;

@Mapper
public interface UserRoleMapper {

	@Insert("insert into user_role (user_id, role_id) values (#{id}, #{role.id})")
	public void addUserRole(@Param("id") Long id, @Param("role") Role role);

	@Delete("delete from user_role where user_id = #{id}")
	public void removeAllUserRoles(Long id);
	@Select("select r.* from role r join user_role ur on r.id = ur.role_id where ur.user_id = #{userId} and r.status=1")
	public List<Role> getRolesByUserId(Long userId);



}
