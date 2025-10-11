package com.afr.fms.Checker.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Maker.entity.Tax;

@Mapper
public interface ManageTaxMapper {

    // and from_ = #{branch_id} 
    @Select("SELECT * FROM tblTaxable WHERE status = 1 ")
    public List<Tax> getPendingTaxes(PaginatorPayLoad paginatorPayLoad);

//and from_ = #{branch_id}
    @Select("SELECT * FROM tblTaxable WHERE status = 2 ")
    public List<Tax> getRejectedTaxes(PaginatorPayLoad paginatorPayLoad);

    //and from_ = #{branch_id}
    @Select("SELECT * FROM tblTaxable WHERE status = 5 ")
    public List<Tax> getApprovedTaxes(PaginatorPayLoad paginatorPayLoad);

    @Update("UPDATE tblTaxable SET status = 4, checker_name = #{checker_name}, checked_Date = CURRENT_TIMESTAMP WHERE id = #{id}")
    public void reviewTaxes(Tax tax);

    @Update("UPDATE tblTaxable SET status = 2, checker_rejected_reason = #{checker_rejected_reason}, rejector_checker_id = #{rejector_checker_id}, checker_rejected_date = CURRENT_TIMESTAMP WHERE id = #{id}")
    public void rejectTax(Tax tax);

}
