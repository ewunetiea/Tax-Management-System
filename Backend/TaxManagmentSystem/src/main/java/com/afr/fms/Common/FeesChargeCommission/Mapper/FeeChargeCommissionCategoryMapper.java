package com.afr.fms.Common.FeesChargeCommission.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Common.FeesChargeCommission.Model.FeeChargeCommissionCategory;

@Mapper
public interface FeeChargeCommissionCategoryMapper {


    @Select("insert into fee_charge_commission_category_mgt( category, created_date, user_id) values(#{category}, CURRENT_TIMESTAMP, #{user.id})")
    public void createFeeChargeCommissionCategory(FeeChargeCommissionCategory FeeChargeCommissionCategory);

    
    @Select("select * from fee_charge_commission_category_mgt ")
    @Results(value = {
            @Result(property = "id", column = "id"),

            @Result(property = "feetype", column = "id", one = @One(select = "com.afr.fms.Common.FeesChargeCommission.Mapper.FeeChargeCommissionTypeMapper.getFeeChargeCommissionTypeByCategoryId")),

    })
    public List<FeeChargeCommissionCategory> getFeeChargeCommissionCategory();

    @Update("update fee_charge_commission_category_mgt set category=#{category}, modified_date = CURRENT_TIMESTAMP, user_id=#{user.id} where id=#{id}")
    public void updateFeeChargeCommissionCategory(FeeChargeCommissionCategory FeeChargeCommissionCategory);

    @Delete("delete from  fee_charge_commission_category_mgt  where id = #{id}")
    public void deleteFeeChargeCommissionCategory(Long id);

    @Select("select * from fee_charge_commission_category_mgt where id  = #{id}")

    public FeeChargeCommissionCategory getFeeChargeCommissionCategoryById(Long id);

}
