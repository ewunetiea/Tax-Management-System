package com.afr.fms.Security.Password;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PasswordMapper {

    @Update("update [user] set password = #{password} where id = #{id}")
	public void changePassword(ChangePasswordDto passDto);

	@Select("select password from [user] where id = #{id}")
	public String getUserPassword(Long id);

	@Update("update [user] set password = #{param1} where id = #{param2}")
	public void changeMyPassword(String password, Long id); 

	@Update("update user_security set password_modified_date = CURRENT_TIMESTAMP, credentialsNonExpired=1 where user_id = #{param1}")
	public void updateUserSecurity(Long id); 
}
