package com.afr.fms.Approver.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import com.afr.fms.Maker.entity.Tax;

@Mapper
public interface ManageTaxApproverMapper {
    
    @Update("UPDATE tblTaxable SET status = 5, approver_name = #{approver_name}, approved_date = CURRENT_TIMESTAMP WHERE id = #{id}")
    public void approveTaxes(Tax tax);

    @Update("UPDATE tblTaxable SET status = 3, approver_rejected_reason = #{checker_rejected_reason}, rejector_approver_id = #{user_id}, approver_rejected_date = CURRENT_TIMESTAMP WHERE id = #{id}")
    public void rejectTax(Tax tax);
}
