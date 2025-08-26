package com.afr.fms.Common.RecentActivity;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.afr.fms.Common.Entity.Report;
import org.apache.commons.lang3.StringUtils;

@Mapper
public interface RecentActivityMapper {

        @Insert("insert into recent_activity (message, created_date, user_id) values (#{message}, CURRENT_TIMESTAMP, #{user.id})")
        public void addRecentActivity(RecentActivity ra);

        @Select("select top 5 * from recent_activity where user_id = #{user_id} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getAllRecentActivity(Long user_id);

        @Select("select top 10 * from recent_activity where user_id = #{user_id} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getAllAdminRecentActivity(Long user_id);

        @Select("select * from recent_activity rc where rc.created_date BETWEEN #{startDateTime} AND #{endDateTime} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getAllRecentActivityAdmin(Report report);

        @Select("select * from recent_activity rc where rc.user_id=#{user_id} and  rc.created_date BETWEEN #{startDateTime} AND #{endDateTime} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getAllRecentActivityByUserId(Report report);

        @Select("select * from recent_activity rc where rc.user_id=#{user_id} and rc.message like concat('%',#{content},'%') ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByContent(Report report);

        @Select("select * from recent_activity rc where rc.user_id=#{user_id} and  rc.created_date BETWEEN #{action_date[0]} AND #{action_date[1]} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByDateRange(Report report);

        @Select("select * from recent_activity rc where rc.user_id=#{user_id} and rc.message like concat('%',#{content},'%') and  rc.created_date BETWEEN #{action_date[0]} AND #{action_date[1]} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByDateRangeandContent(Report report);

        @Select("select * from recent_activity rc where rc.user_id in (#{user_ids}) ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByUserIDs(String user_ids);

        @Select("select * from recent_activity rc where rc.user_id in (#{user_ids})  and rc.message like concat('%',#{report.content},'%') ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByUserIDsandContent(Report report, String user_ids);

        @Select("select * from recent_activity rc where rc.user_id in (#{user_ids}) and rc.created_date BETWEEN #{report.action_date[0]} AND #{report.action_date[1]} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByUserIDsandActionDate(Report report, String user_ids);

        @Select("select * from recent_activity rc where rc.user_id in (#{user_ids}) and rc.message like concat('%',#{report.content},'%') and rc.created_date BETWEEN #{report.action_date[0]} AND #{report.action_date[1]} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByUserIDsandContentandActionDate(Report report, String user_ids);

        @Select("select * from recent_activity rc where  rc.message like concat('%',#{content},'%') ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByContentAdmin(Report report);

        @Select("select * from recent_activity rc where   rc.created_date BETWEEN #{action_date[0]} AND #{action_date[1]} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByDateRangeAdmin(Report report);

        @Select("select * from recent_activity rc where  rc.message like concat('%',#{content},'%') and  rc.created_date BETWEEN #{action_date[0]} AND #{action_date[1]} ORDER BY id DESC")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "user", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
        })
        public List<RecentActivity> getActivityByDateRangeandContentAdmin(Report report);
}
