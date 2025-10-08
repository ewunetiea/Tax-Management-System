package com.afr.fms.Checker.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Maker.entity.Tax;

@Mapper
public interface ManageTaxMapper {

    @Select("SELECT * FROM tblTaxable WHERE status = 1 and from_ = #{branch_id} ")
    public List<Tax> getPendingTaxes(PaginatorPayLoad paginatorPayLoad);

    @Select("SELECT * FROM tblTaxable WHERE status = 2 and from_ = #{branch_id}")
    public List<Tax> getRejectedTaxes(PaginatorPayLoad paginatorPayLoad);

    @Select("SELECT * FROM tblTaxable WHERE status = 5 and from_ = #{branch_id}")
    public List<Tax> getApprovedTaxes(PaginatorPayLoad paginatorPayLoad);

    @Update("UPDATE tblTaxable SET status = 4 WHERE mainGuid = #{mainGuid}")
    public void reviewTaxes(Tax tax);

}
