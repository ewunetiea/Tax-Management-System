package com.afr.fms.Auditor.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UploadFileISMMapper {

	@Select("insert into upload_file_ism (ism_audit_id, file_url, uploaded_date) OUTPUT inserted.id values(#{branch_audit_id}, #{file_url}, CURRENT_TIMESTAMP)")
	public Long InsertFileUrl(String file_url, Long branch_audit_id);

	@Delete("delete from upload_file_ism where ism_audit_id = #{ism_audit_id}")
	public void removeFileUrls(Long ism_audit_id);

	@Select("select file_url from upload_file_ism where ism_audit_id = #{ism_audit_id}")
	public List<String>  getFileUrlsByAuditID(Long ism_audit_id);

}
