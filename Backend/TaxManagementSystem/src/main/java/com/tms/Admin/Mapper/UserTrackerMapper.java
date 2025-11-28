package com.tms.Admin.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import com.tms.Admin.Entity.UserTracker;

@Mapper
public interface UserTrackerMapper {
    @Select("insert into user_tracker(user_name,user_agent,ip_address,login_time,status) OUTPUT inserted.id values(#{user_name},#{user_agent},#{ip_address},CURRENT_TIMESTAMP,1)")
    public Long registerOnlineUser(String user_name, String user_agent, String ip_address);

    @Insert("insert into user_tracker(user_name,user_agent,ip_address,login_time,status) values(#{user_name},#{user_agent},#{ip_address},CURRENT_TIMESTAMP,2)")
    public Long registerUnAutorizedUsers(String user_name, String user_agent, String ip_address);

    @Select("SELECT COUNT(*) FROM user_tracker WHERE user_name = #{user_name}")
    public int isUserSessionExist(String user_name);

    @Update("update user_tracker set status=0 where id=#{id}")
    public void registerOfflineUser(Long id);

    @Select("select * from user_tracker where status = 1 or status = 2 order by id DESC")
    public List<UserTracker> getOnlineFailedUsers();

    @Select("select id from user_tracker where status = 1 and user_name = #{username} and ip_address = #{ip_address}")
    public List<Long> getUserInfo(String username, String ip_address);

    @Select("select * from user_tracker where status = 1")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_name", one = @One(select = "com.tms.Admin.Mapper.UserMapper.findByEmail"))
    })
    public List<UserTracker> getOnlineUsers();

    @Delete("delete from user_tracker where id = #{id}")
    public void updateLoginStatus(UserTracker userTracker);

    @Delete("delete from refresh_token where user_tracker_id = #{user_tracker_id}")
    public void deleteRefreshToken(Long user_tracker_id);

    @Delete("DELETE FROM refresh_token WHERE user_tracker_id IN (SELECT id FROM user_tracker WHERE user_name = #{user_name}); "
            +
            "DELETE FROM user_tracker WHERE user_name = #{user_name}")
    public void removeUserSessions(String user_name);

}
