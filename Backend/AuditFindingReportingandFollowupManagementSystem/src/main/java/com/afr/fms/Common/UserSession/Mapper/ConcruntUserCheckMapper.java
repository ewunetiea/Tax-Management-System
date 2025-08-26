package com.afr.fms.Common.UserSession.Mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Common.UserSession.Entity.UserSession;
@Mapper

public interface ConcruntUserCheckMapper {
   
    @Select("SELECT * FROM concrunet_user_login WHERE user_id = #{user_id}")
    public UserSession getUserSessionById(Long user_id);

    @Select("SELECT COUNT(1) FROM concrunet_user_login WHERE user_id = #{user_id}")
    public Long sessionExists(Long user_id);

    @Update("UPDATE concrunet_user_login SET jwt_token = #{jwt_token}, login_time = #{login_time}, username=#{username} WHERE user_id = #{user_id}")
    public void updateSession(UserSession session);

    @Insert("INSERT INTO concrunet_user_login (user_id, jwt_token, login_time , username) VALUES (#{user_id}, #{jwt_token}, #{login_time}, #{username})")
    public void insertSession(UserSession session);

    @Delete("DELETE FROM concrunet_user_login WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);

}
