package com.afr.fms.Common.FeesChargeCommission.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Common.FeesChargeCommission.Mapper.FeeChargeCommissionCategoryMapper;
import com.afr.fms.Common.FeesChargeCommission.Model.FeeChargeCommissionCategory;

@Service
public class FeeChargeCommissionCategoryService {
    @Autowired
    private FeeChargeCommissionCategoryMapper feeChargeCommissionCategoryMapper;

    public void createFeeChrgeCommisionCategory(FeeChargeCommissionCategory feeChargeCommissionCategory) {

        feeChargeCommissionCategoryMapper.createFeeChargeCommissionCategory(feeChargeCommissionCategory);

    }

    public List<FeeChargeCommissionCategory> getFeeChargeCommissionCategory() {
        return feeChargeCommissionCategoryMapper.getFeeChargeCommissionCategory();
    }


    public FeeChargeCommissionCategory getFeeChargeCommissionCategoryById(Long id) {
        return feeChargeCommissionCategoryMapper.getFeeChargeCommissionCategoryById(id);
    }


    public void updateFeeChargeCommissionCategory(FeeChargeCommissionCategory feeChargeCommissionCategory) {
        feeChargeCommissionCategoryMapper.updateFeeChargeCommissionCategory(feeChargeCommissionCategory);
    }

    public void deleteFeeChargeCommissionCategory(Long id) {

        feeChargeCommissionCategoryMapper.deleteFeeChargeCommissionCategory(id);

    }

}
