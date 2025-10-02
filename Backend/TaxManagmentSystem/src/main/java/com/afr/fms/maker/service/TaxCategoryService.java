package com.afr.fms.Maker.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Maker.entity.TaxCategory;
import com.afr.fms.Maker.mapper.TaxCategoryMapper;

@Service
public class TaxCategoryService {

    @Autowired
    private TaxCategoryMapper taxCategoryMapper;

    public void createTaxCategory(TaxCategory tax) {
        taxCategoryMapper.createTaxCategory(tax);
    }

    public void updateTaxCategory(TaxCategory tax) {
        taxCategoryMapper.updateTaxCategory(tax);
    }

    public List<TaxCategory> getTaxCategories() {
        return taxCategoryMapper.getTaxCategories();
    }

}
