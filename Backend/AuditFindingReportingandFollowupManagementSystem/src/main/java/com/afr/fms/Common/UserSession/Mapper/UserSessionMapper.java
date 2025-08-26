package com.afr.fms.Common.UserSession.Mapper;

import com.afr.fms.Common.UserSession.Entity.UserSession;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserSessionMapper {
        @Insert("INSERT INTO user_sessions (username, jwt_token, login_time, browser_info, ip_address, active, session_id, user_id) "
                        +
                        "VALUES (#{username}, #{jwt_token}, #{login_time}, #{browser_info}, #{ip_address}, #{active}, #{session_id} ,#{user_id})")

        void insertSession(UserSession session);

        @Update("UPDATE user_sessions SET logout_time = #{logout_time}, active = #{active} " +
                        "WHERE username = #{username} AND jwt_token = #{jwt_token}")
        void updateSession(UserSession session);

        @Select("SELECT * FROM user_sessions WHERE username = #{username}")
        List<UserSession> findByUsername(String username);

        @Select("SELECT * FROM user_sessions WHERE username = #{username} AND active = true")
        List<UserSession> findActiveSessionsByUsername(String username);

        @Select("SELECT * FROM user_sessions WHERE username = #{username} AND jwt_token = #{jwt_token}")
        UserSession getUserSessionByUsernameAndToken(@Param("username") String username,
                        @Param("jwt_token") String jwt_token);

        @Select("SELECT TOP 1 * FROM user_sessions WHERE username = #{username} ORDER BY login_time DESC")
        UserSession getUserSessionByUsername(String username);

        @Update("UPDATE user_sessions SET active = 0, logout_time = CURRENT_TIMESTAMP " +
                        "WHERE username = #{username}")
        void invalidateAllSessions(String username);

        @Update("UPDATE user_sessions SET active = 0, logout_time = CURRENT_TIMESTAMP " +
                        "WHERE username = #{username} AND jwt_token = #{jwt_token}")
        void invalidateSession(@Param("username") String username, @Param("jwt_token") String jwt_token);


        @Select("SELECT * FROM user_sessions WHERE user_id = #{user_id}")
        public UserSession getUserSessionById(Long user_id);
    
        @Select("SELECT COUNT(1) FROM user_sessions WHERE user_id = #{user_id}")
        public Long sessionExists(Long user_id);
    
}