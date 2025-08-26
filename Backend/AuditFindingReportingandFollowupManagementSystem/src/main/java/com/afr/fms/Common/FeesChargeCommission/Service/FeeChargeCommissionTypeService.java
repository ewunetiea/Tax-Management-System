package com.afr.fms.Common.FeesChargeCommission.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Common.FeesChargeCommission.Mapper.FeeChargeCommissionTypeMapper;
import com.afr.fms.Common.FeesChargeCommission.Model.FeeChargeCommissionCategory;
import com.afr.fms.Common.FeesChargeCommission.Model.FeeChargeCommissionType;

@Service
public class FeeChargeCommissionTypeService {
    @Autowired
    private FeeChargeCommissionTypeMapper feeChargeCommissionTypeMapper;


    public List<FeeChargeCommissionType> FeeChargeCommissionType() {
        return feeChargeCommissionTypeMapper.getFeeChargeCommissionTypes();
    }

 

    public void updateFeeChargeCommissionType(FeeChargeCommissionType feeChargeCommissionType) {
        feeChargeCommissionTypeMapper.updateFeeChargeCommissionType(feeChargeCommissionType);
    }

    public void deleteFeeChargeCommissionType(Long id) {
        feeChargeCommissionTypeMapper.deleteFeeChargeCommissionType(id);

    }



    public void createCommonfeeChargeCommissionType(FeeChargeCommissionType feeChargeCommissionType) {

        feeChargeCommissionTypeMapper.createCommonfeeChargeCommossionType(feeChargeCommissionType);

    }


   


    public FeeChargeCommissionType getFeeChargeCommissionCTypeById(Long id) {
        return feeChargeCommissionTypeMapper.getFeeChargeCommissionType(id);
    }


  
}
