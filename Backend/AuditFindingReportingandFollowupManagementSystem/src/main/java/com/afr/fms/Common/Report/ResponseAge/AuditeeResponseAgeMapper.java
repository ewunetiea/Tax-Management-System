package com.afr.fms.Common.Report.ResponseAge;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.Report;

@Mapper
public interface AuditeeResponseAgeMapper {

        @Select("select * from IS_Management_Audit ima " + //
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1 " + //
                        " where (ima.approved_date between #{action_date[0]} and #{action_date[1]}) and DATEDIFF(day, ima.approved_date, getDate()) between #{age_range[0]} and #{age_range[1]}  and ima.category = #{user.category} and ima.approve_status=1")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees"))
        })
        public List<AuditISM> getAuditsByAgeRangeandApprovedDate(Report report);

        @Select("select * from IS_Management_Audit ima " +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1 " +
                        " where DATEDIFF(day, ima.approved_date, getDate()) between #{age_range[0]} and #{age_range[1]}  and ima.category = #{user.category} and ima.approve_status=1")

        // @Select("select * from IS_Management_Audit where DATEDIFF(day, approved_date,
        // getDate()) between #{ age_range[0]} and #{age_range[1]} and category =
        // #{user.category} and approve_status=1 and id in (select IS_MGT_id from
        // IS_MGT_Auditee where complete_status != 1)")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees"))
        })
        public List<AuditISM> getAuditsByAgeRange(Report report);

        @Select("select * from IS_Management_Audit ima " +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1 " +
                        " where (ima.approved_date between #{action_date[0]} and #{action_date[1]}) and (DATEDIFF(day, ima.approved_date, getDate()) = #{age})  and ima.category = #{user.category} and ima.approve_status=1")

        // @Select("select * from IS_Management_Audit where (approved_date between
        // #{action_date[0]} and #{action_date[1]}) and (DATEDIFF(day, approved_date,
        // getDate()) = #{age}) and category = #{user.category} and approve_status=1 and
        // id in (select IS_MGT_id from IS_MGT_Auditee where complete_status != 1)")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees"))
        })
        public List<AuditISM> getAuditsByAgeandApprovedDate(Report report);

        @Select("select * from IS_Management_Audit ima " +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1 " +
                        " where (DATEDIFF(day, ima.approved_date, getDate()) = #{age})  and ima.category = #{user.category} and ima.approve_status=1")

        // @Select("select * from IS_Management_Audit where (DATEDIFF(day,
        // approved_date, getDate()) = #{age}) and category = #{user.category} and
        // approve_status=1 and id in (select IS_MGT_id from IS_MGT_Auditee where
        // complete_status != 1)")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees"))
        })
        public List<AuditISM> getAuditsByAge(Report report);

        @Select("select * from IS_Management_Audit ima " +
                        " inner join IS_MGT_Auditee isma on isma.IS_MGT_id = ima.id and isma.complete_status != 1 " +
                        " where (approved_date between #{action_date[0]} and #{action_date[1]})  and ima.category = #{user.category} and ima.approve_status=1")

        // @Select("select * from IS_Management_Audit where (approved_date between
        // #{action_date[0]} and #{action_date[1]}) and category = #{user.category} and
        // approve_status=1 and id in (select IS_MGT_id from IS_MGT_Auditee where
        // complete_status != 1)")
        @Results(value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "auditor", column = "auditor_id", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
                        @Result(property = "IS_MGTAuditee", column = "id", many = @Many(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getISMAuditees"))
        })
        public List<AuditISM> getAuditsByApprovedDate(Report report);

}
