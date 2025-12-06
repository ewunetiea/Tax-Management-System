package com.tms.Common.RecentActivity;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.tms.Admin.Entity.User;

@Mapper
public interface RecentActivityMapper {

        @Insert("insert into recent_activity (message, created_date, user_id) values (#{message}, CURRENT_TIMESTAMP, #{user.id})")
        public void addRecentActivity(RecentActivity ra);

        @Select("select top 5 * from recent_activity where user_id = #{user_id} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.tms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getAllRecentActivity(Long user_id);

        @Select("select * from [user] ORDER BY email DESC")
        public List<User> getUsers();

     @Select({
    "<script>",
    "SELECT * FROM recent_activity rc",
    "WHERE 1=1",

    "<if test='user_ids != null and !user_ids.isEmpty()'>",
    "   AND rc.user_id IN",
    "   <foreach collection='user_ids' item='id' open='(' separator=',' close=')'>",
    "       #{id}",
    "   </foreach>",
    "</if>",

    "<if test='message != null and message.trim() != \"\"'>",
    "   AND rc.message LIKE concat('%', #{message}, '%')",
    "</if>",

    "<if test='action_date != null and action_date.size() >= 1'>",

        "<!-- BOTH START & END DATE PRESENT -->",
        "<if test='action_date[0] != null and action_date[1] != null'>",
        "   AND CAST(rc.created_date AS DATE) BETWEEN CAST(#{action_date[0]} AS DATE) AND CAST(#{action_date[1]} AS DATE)",
        "</if>",

        "<!-- ONLY START DATE PRESENT -->",
        "<if test='action_date[0] != null and action_date[1] == null'>",
        "   AND CAST(rc.created_date AS DATE) = CAST(#{action_date[0]} AS DATE)",
        "</if>",

        "<!-- ONLY END DATE PRESENT -->",
        "<if test='action_date[0] == null and action_date[1] != null'>",
        "   AND CAST(rc.created_date AS DATE) = CAST(#{action_date[1]} AS DATE)",
        "</if>",

    "</if>",

    "ORDER BY rc.id DESC",
    "</script>"
})
@Results({
    @Result(property = "id", column = "id"),
    @Result(property = "user", column = "user_id", one = @One(select = "com.tms.Admin.Mapper.UserMapper.getUserById"))
})
public List<RecentActivity> generateRecentActivities(
    @Param("user_ids") List<Long> user_ids,
    @Param("message") String message,
    @Param("action_date") List<Date> action_date
);



        @Select({
                        "<script>",
                        "SELECT * FROM recent_activity rc",
                        "WHERE rc.user_id = #{user_id}",
                        "<if test='message != null and message.trim() != \"\"'>",
                        "AND rc.message LIKE concat('%', #{message}, '%')",
                        "</if>",
                        "<if test='action_date != null and action_date.size() == 2'>",
                        "AND rc.created_date BETWEEN #{action_date[0]} AND #{action_date[1]}",
                        "</if>",
                        "ORDER BY rc.id DESC",
                        "</script>"
        })

        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.tms.Admin.Mapper.UserMapper.getUserById")),
        })

        public List<RecentActivity> getAllRecentActivities(@Param("user_id") Long user_id,
                        @Param("message") String message, @Param("action_date") List<Date> action_date);

        @Select("select * from recent_activity where user_id = #{user_id} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.tms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getAllRecentActivityByUserId(Long user_id);

  @Select({
    "<script>",
    "SELECT * FROM recent_activity rc",
    "WHERE 1=1",

    "<if test='user_id != null'>",
    "   AND rc.user_id = #{user_id}",
    "</if>",

    "<if test='message != null and message.trim() != \"\"'>",
    "   AND rc.message LIKE concat('%', #{message}, '%')",
    "</if>",

    "<if test='action_date != null and action_date.size() >= 1'>",

        "<!-- BOTH START & END DATE PRESENT -->",
        "<if test='action_date[0] != null and action_date[1] != null'>",
        "   AND CAST(rc.created_date AS DATE) BETWEEN CAST(#{action_date[0]} AS DATE) AND CAST(#{action_date[1]} AS DATE)",
        "</if>",

        "<!-- ONLY START DATE PRESENT -->",
        "<if test='action_date[0] != null and action_date[1] == null'>",
        "   AND CAST(rc.created_date AS DATE) = CAST(#{action_date[0]} AS DATE)",
        "</if>",

        "<!-- ONLY END DATE PRESENT -->",
        "<if test='action_date[0] == null and action_date[1] != null'>",
        "   AND CAST(rc.created_date AS DATE) = CAST(#{action_date[1]} AS DATE)",
        "</if>",

    "</if>",

    "ORDER BY rc.id DESC",
    "</script>"
})



        @Results({
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.tms.Admin.Mapper.UserMapper.getUserById"))
        })
        public List<RecentActivity> generateRecentActivitiesByDateAndContent(
                        @Param("user_id") Long user_id,
                        @Param("message") String message,
                        @Param("action_date") List<Date> action_date);

}
