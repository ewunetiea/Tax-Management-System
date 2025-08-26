package com.afr.fms.Common.Audit_Remark;

import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface RemarkMapper {
        @Insert("INSERT INTO remark_ism(audit_id, sender, reciever, message, remark_date,status, rejected) VALUES (#{audit.id}, #{sender.id}, #{reciever.id}, #{message}, CURRENT_TIMESTAMP,0,0)")
        public void addRemark(Remark remark);

        @Insert("INSERT INTO remark_ism(audit_id, sender, reciever, message, remark_date, status, rejected) VALUES (#{audit.id}, #{sender.id}, #{reciever.id}, #{message}, CURRENT_TIMESTAMP,0,1)")
        public void addRejectedRemark(Remark remark);

        @Update("update remark_ism set status=1 where audit_id=#{audit.id} and reciever=#{reciever.id} and sender = #{sender.id}")
        public void seenRemark(Remark remark);

        @Select("Select * from remark_ism where audit_id = #{audit.id} and (sender = #{sender.id} or sender = #{reciever.id}) and (reciever = #{sender.id} or reciever = #{reciever.id}) ORDER BY remark_date")
        @Results(value = {
                        @Result(property = "audit", column = "audit_id", one = @One(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAudit")),
                        @Result(property = "sender", column = "sender", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
                        @Result(property = "reciever", column = "reciever", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById"))
        })
        public List<Remark> getRemarks(Remark remark);

        @Select("Select * from remark_ism where audit_id=#{audit.id} AND (status=0 AND reciever=#{reciever.id}) ORDER BY remark_date")
        @Results(value = {
                        @Result(property = "audit", column = "audit_id", one = @One(select = "com.afr.fms.Auditor.Mapper.AuditISMMapper.getAudit")),
                        @Result(property = "sender", column = "sender", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById")),
                        @Result(property = "reciever", column = "reciever", one = @One(select = "com.afr.fms.Admin.Mapper.UserMapper.getUserById"))
        })
        public List<Remark> getUnseenRemarks(Remark remark);

}
