package com.afr.fms.Maker.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.afr.fms.Maker.entity.TaxFile;

@Mapper
public interface TaxFileMapper {

        @Select("SELECT TOP (1000) Id, FileName as fileName, Extension as extension, SupportId as supportId "
                        + "FROM FileDetailOfClaim")
        public List<TaxFile> getAllFiles();

        // Insert new file detail
        @Insert("INSERT INTO  FileDetailOfClaim " +
                        "(Id, FileName, Extension, SupportId, tax_id) " +
                        "VALUES (#{Id}, #{fileName}, #{extension}, #{supportId}, #{tax_id})")
        public void insertFile(TaxFile file);

        @Select("SELECT  FileName as fileName, Extension as extension " +
  "FROM FileDetailOfClaim " +
                        "WHERE tax_id = #{Id}")
    public   List<TaxFile> getFileByFileById ( Long Id);

   


}
