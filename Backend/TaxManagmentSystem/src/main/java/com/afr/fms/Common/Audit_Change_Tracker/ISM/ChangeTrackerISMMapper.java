package com.afr.fms.Common.Audit_Change_Tracker.ISM;

import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface ChangeTrackerISMMapper {

	@Insert("insert into change_tracker_ISM(user_id, change, content_type, audit_id, change_date) values(#{changer.id}, #{change}, #{content_type}, #{audit_id}, CURRENT_TIMESTAMP)")
	public void insertChange(Change_Tracker_ISM change_Tracker_ISM);

	@Select("select * from change_tracker_ISM where audit_id = #{audit_id} order by change_date")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "changer", column = "user_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getAuditorById")),
	})
	public List<Change_Tracker_ISM> getChanges(Long audit_id);



	@Insert("insert into change_tracker_INS(user_id, change, content_type, audit_id, change_date) values(#{changer.id}, #{change}, #{content_type}, #{audit_id}, CURRENT_TIMESTAMP)")
	public void insertChangeINS(Change_Tracker_ISM change_Tracker_ISM);

}
