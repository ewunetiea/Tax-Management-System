package com.afr.fms.Common.Finding;

import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FindingMapper {

    @Select("insert into finding( content, created_date, modified_date, user_id,identifier) values(#{content}, CURRENT_TIMESTAMP, #{modified_date}, #{user.id},#{identifier})")
    public Long createCommonFinding(Finding commonFinding);

    @Select("select * from finding where user_id=#{id} order by created_date DESC")
    public List<Finding> getCommonFinding(Long id);

    @Select("select * from finding where identifier = #{identifier} order by created_date DESC")
    public List<Finding> getCommonFindings(String identifier);

    @Update("update finding set content=#{content}, identifier = #{identifier},  modified_date = CURRENT_TIMESTAMP, user_id=#{user.id} where id=#{id}")
    public void updateCommonFinding(Finding commonFinding);

    @Delete("delete from  finding  where id = #{id}")
    public void deleteCommonFinding(Long id);

}
