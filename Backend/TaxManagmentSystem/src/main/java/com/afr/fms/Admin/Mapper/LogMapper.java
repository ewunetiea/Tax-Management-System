package com.afr.fms.Admin.Mapper;
import com.afr.fms.Admin.Entity.Log;
import java.util.List;
import org.apache.ibatis.annotations.*;
@Mapper
public interface LogMapper {

	@Select("select * from log Order By log_time")
	public List<Log> getLogRecords();

	@Delete("delete from log where id= #{id}")
	public void	deleteLogRecordById(Long id);

    @Insert("insert into log(name,exception,log_time) values(#{name},#{exception},CURRENT_TIMESTAMP)")
    public void addLog(Log log);
}
