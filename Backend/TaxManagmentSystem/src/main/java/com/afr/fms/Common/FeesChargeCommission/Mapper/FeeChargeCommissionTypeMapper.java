package com.afr.fms.Common.FeesChargeCommission.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.afr.fms.Common.FeesChargeCommission.Model.FeeChargeCommissionType;

@Mapper
public interface FeeChargeCommissionTypeMapper {

  @Select("insert into fee_charge_commossion_type_mgt( type, created_date, user_id, category_id) values(#{type}, CURRENT_TIMESTAMP, #{user.id}, #{category_id})")
  public void createCommonfeeChargeCommossionType(FeeChargeCommissionType feeChargeCommissionType);

  @Select("SELECT fcct.type, fcct.id, fcct.created_date, fccc.category AS category " +
      "FROM fee_charge_commossion_type_mgt fcct " +
      "INNER JOIN fee_charge_commission_category_mgt fccc ON fcct.category_id = fccc.id " +
      "ORDER BY type ASC")
  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "type", column = "type"),
      @Result(property = "created_date", column = "created_date"),
      @Result(property = "category", column = "category") // <-- this is required
  })
  List<FeeChargeCommissionType> getFeeChargeCommissionTypes();

  @Update("update fee_charge_commossion_type_mgt set type=#{type},  modified_date = CURRENT_TIMESTAMP, user_id=#{user.id} where id=#{id}")
  public void updateFeeChargeCommissionType(FeeChargeCommissionType feeChargeCommissionType);

  @Delete("delete from  fee_charge_commossion_type_mgt  where id = #{id}")
  public void deleteFeeChargeCommissionType(Long id);

  @Select("select * from fee_charge_commossion_type_mgt where id  = #{id}")

  public FeeChargeCommissionType getFeeChargeCommissionType(Long id);


  @Select("select * from fee_charge_commossion_type_mgt where category_id  = #{id}")

  public FeeChargeCommissionType getFeeChargeCommissionTypeByCategoryId(Long id);

}
