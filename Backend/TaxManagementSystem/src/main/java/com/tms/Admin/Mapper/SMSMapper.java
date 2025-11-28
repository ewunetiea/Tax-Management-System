package com.tms.Admin.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.tms.Admin.Entity.SMS;

@Mapper
public interface SMSMapper {
	@Insert("insert into SMS(user_name, password, message, status, system_type)values(#{user_name}, #{password}, #{message}, #{status}, #{system_type})")
	public void createSMS(SMS sms);

	@Select("select * from SMS Order By user_name")
	public List<SMS> getSMS();

	@Select("select * from SMS where status = 1 and system_type = #{system_type} Order By system_type")
	public List<SMS> getActiveSMS(String system_type);

	@Update("update SMS set user_name=#{user_name}, system_type=#{system_type}, password=#{password}, message=#{message}, status=#{status} where id=#{id}")
	public void updateSMS(SMS sms);

	@Update("update SMS set status=#{status} where id=#{id}")
	public void manageSMSStatus(SMS sms);
}
