package com.afr.fms.Admin.Mapper;

import com.afr.fms.Admin.Entity.Schedule;

import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface ScheduleMapper {
    @Select("select * from schedule ORDER BY name")
    @Results(value = {
            @Result(property = "id", column = "id")
    })
    public List<Schedule> getSchedules();

    @Update("update schedule set status = #{status} where id = #{id}")
    public void updateScheduleStatusById(@Param("id") Long id, @Param("status") int status);

    @Select("select * from schedule where name = #{name} and status = 1")
    public Schedule checkScheduleStatus(String name);

}
