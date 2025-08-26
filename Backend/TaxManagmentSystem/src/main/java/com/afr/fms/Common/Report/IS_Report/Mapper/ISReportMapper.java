package com.afr.fms.Common.Report.IS_Report.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;

@Mapper
public interface ISReportMapper {

	@Select("select * from IS_MGT_Auditee where auditee_id = #{directorate_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "auditee_id", column = "auditee_id"),
			@Result(property = "auditee", column = "auditee_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
			@Result(property = "auditeeDivisionISM", column = "id", many = @Many(select = "com.afr.fms.Common.Report.IS_Report.Mapper.ISReportMapper.getDivisionByDirectorateId")),
	})
	public List<IS_MGT_Auditee> getDivisions(Long directorate_id);

	@Select("select * from Audtiee_Division_ISM where IS_MGT_Auditee_id = #{IS_MGT_Auditee_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "division", column = "division_id", one = @One(select = "com.afr.fms.Admin.Mapper.BranchMapper.getBranchById")),
	})
	public List<AuditeeDivisionISM> getDivisionByDirectorateId(Long IS_MGT_Auditee_id);

}
