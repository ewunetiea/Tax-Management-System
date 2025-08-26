package com.afr.fms.Security.UserSecurity.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.afr.fms.Admin.Entity.NotifyAdmin;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Security.UserSecurity.entity.UserSecurity;

@Mapper
public interface UserSecurityMapper {

	@Update("update user_security set number_of_attempts = #{number_of_attempts}, lock_time = #{lock_time}, accountNonLocked = #{accountNonLocked} where user_id=#{user_id}")
	public void updateAccountLockInfo(UserSecurity us);

	@Update("update user_security set credentialsNonExpired = #{credentialsNonExpired} where user_id=#{user_id}")
	public void updateCredentialStatus(UserSecurity us);

	@Select("select top 1 * from user_security where user_id = #{user_id}")
	public UserSecurity getUserSecurityInfoByUserId(Long user_id);

	// admin notification
	@Insert("insert into admin_notification (user_name, failed , failed_status) values (#{user_name}, 1,1)")
	public void addFailedUserName(String user_name);

	@Insert("insert into admin_notification (user_name, locked ,  locked_status) values (#{user_name}, 1,1)")
	public void addLockedUserName(String user_name);

	@Insert("insert into admin_notification (user_name, expired,  expired_status) values (#{user_name}, 1,1)")
	public void addExpiredUserName(String user_name);

	@Select("select  * from admin_notification where failed_status=1 or expired_status=1 or locked_status=1  ORDER BY (id) DESC")
	public List<NotifyAdmin> notifyAdmin();

	@Update("update admin_notification set failed_status=2 where id=#{id}")
	public void viewFailedNotifications(Long id);

	@Update("update admin_notification set expired_status=2 where id=#{id}")

	public void viewExpiredNotifications(Long id);

	@Update("update admin_notification set locked_status=2 where id=#{id}")

	public void viewLockedNotifications(Long id);

	@Select("select * from admin_notification where user_name=#{user_name}")
	public List<NotifyAdmin> isUsernameExist(String user_name);

	@Update("update admin_notification set failed_status=1 where user_name=#{user_name}")
	public void updateFailedAttempts(String user_name);

	@Update("update admin_notification set locked=1, locked_status=1 where user_name=#{user_name}")
	public void updateLockedStatus(String user_name);

	@Update("update admin_notification set expired = 1, expired_status=1 where user_name=#{user_name}")
	public void updateExpiredStatus(String user_name);

	@Insert("INSERT INTO user_security(password_created_date, number_of_attempts, credentialsNonExpired, accountNonLocked, accountNonExpired, user_id)"
			+ "VALUES (CURRENT_TIMESTAMP,0,1,1,1, #{user_id})")
	public void addUserSecurity(Long user_id);

	@Insert("INSERT INTO user_security(password_created_date, number_of_attempts, credentialsNonExpired, accountNonLocked, accountNonExpired, user_id)"
			+ "VALUES ( #{user_security.password_created_date} ,  #{user_security.number_of_attempts},  #{user_security.credentialsNonExpired},  #{user_security.accountNonLocked},  #{user_security.accountNonExpired}, #{id}) ;"
			+
			" update [user] set status = #{status} where id = #{id}")
	public void insertUserSecurity(User user);

	@Delete("delete from user_security where user_id = #{user_id}")
	public void deleteUserSecurityByUserID(Long user_id);

	@Update("update user_security set accountNonLocked=#{user_security.accountNonLocked} where id=#{user_security.id} ")
	public void unLockAccount(User user);

	@Update("update user_security set accountNonLocked=#{user_security.accountNonLocked}, number_of_attempts = #{user_security.number_of_attempts}, accountNonExpired = #{user_security.accountNonExpired}, credentialsNonExpired = #{user_security.credentialsNonExpired}, password_modified_date = #{user_security.password_modified_date} where id=#{user_security.id};"
			+
			" update [user] set status = #{status} where id = #{id}")
	public void updateUserSecurity(User user);

	@Update("update user_security set accountNonLocked = 1, number_of_attempts = 0 where id=#{user_security.id} ")
	public void updateUnLockAccount(User user);
}
