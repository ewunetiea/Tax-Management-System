package com.tms.Reviewer.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.tms.Common.Entity.PaginatorPayLoad;
import com.tms.Maker.entity.MakerSearchPayload;
import com.tms.Maker.entity.Tax;

@Mapper
public interface ManageTaxReviewerMapper {

  // @Select("SELECT * FROM tblTaxable WHERE status = 0 and from_ = #{branch_id}")
  // public List<Tax> getPendingTaxes(PaginatorPayLoad paginatorPayLoad);

  @Select("SELECT * FROM tblTaxable WHERE status = 0 and from_ = #{branch_id}")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "taxFile", column = "id", many = @Many(select = "com.tms.Maker.mapper.TaxFileMapper.getFileByFileById")),

  })
  public List<Tax> getPendingTaxes(PaginatorPayLoad paginatorPayLoad);

  @Select("SELECT * FROM tblTaxable WHERE (status = 2 or status = 3) and from_ = #{branch_id} ")
  public List<Tax> getRejectedTaxes(PaginatorPayLoad paginatorPayLoad);

  @Select("SELECT * FROM tblTaxable WHERE status = 5 and from_ = #{branch_id}")
  public List<Tax> getApprovedTaxes(PaginatorPayLoad paginatorPayLoad);

  @Select("SELECT * FROM tblTaxable WHERE status = 1 and from_ = #{branch_id}")
  public List<Tax> getSentTaxes(PaginatorPayLoad paginatorPayLoad);

  @Update("UPDATE tblTaxable SET status = 1, checker_name = #{checker_name}, checked_Date = CURRENT_TIMESTAMP WHERE id = #{id}")
  public void reviewTaxes(Tax tax);

  @Update("UPDATE tblTaxable SET status = 2, checker_rejected_reason = #{checker_rejected_reason}, rejector_checker_id = #{user_id}, checker_rejected_date = CURRENT_TIMESTAMP WHERE id = #{id}")
  public void rejectTax(Tax tax);

  @Update("UPDATE tblTaxable SET status = 0, checker_name = null , rejector_checker_id = null , checked_Date = null, checker_rejected_date = null WHERE id = #{id}")
  public void backToWaitingState(Tax tax);

}
