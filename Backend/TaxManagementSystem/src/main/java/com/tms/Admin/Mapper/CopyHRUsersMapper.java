package com.tms.Admin.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import com.tms.Admin.Entity.JobPosition;
import com.tms.Admin.Entity.UserCopyFromHR;

@Mapper
public interface CopyHRUsersMapper {

    @Insert("INSERT INTO users_from_hr_system (empId, position, empName, email, employmentDate, departmentName, deptLocation, unit, status)"
            + " VALUES (#{empId}, #{position}, #{empName}, #{email}, #{employmentDate}, #{departmentName}, #{deptLocation}, #{unit}, #{status})")
    public void addUserCopyHR(UserCopyFromHR user);

    @Select("select top 1 * from users_from_hr_system where empId = #{employee_id}")
    public UserCopyFromHR checkUserEmployeeId(String employee_id);

    @Select("select * from users_from_hr_system where empId = #{employee_id}")
    public List<UserCopyFromHR> checkUserEmployeeId2(String employee_id);

    @Select("select trim(empId) from users_from_hr_system ")
    public List<String> allUsersEmployeeIDs();

    @Delete("delete from users_from_hr_system where empId = #{employee_id}")
    public void deleteByEmployeeId(String employee_id);

    @Select("select * from job_position order by title")
    public List<JobPosition> get_job_positions();
}
