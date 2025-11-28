package com.tms.Maker.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tms.Admin.Entity.User;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;
import com.tms.Maker.entity.TaxCategory;
import com.tms.Maker.mapper.TaxCategoryMapper;

@Service
public class TaxCategoryService {

    @Autowired
    private TaxCategoryMapper taxCategoryMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    public void createTaxCategory(TaxCategory tax) {
        taxCategoryMapper.createTaxCategory(tax);
    }

    public void updateTaxCategory(TaxCategory tax) {
        taxCategoryMapper.updateTaxCategory(tax);
    }

    public List<TaxCategory> getTaxCategories() {
        return taxCategoryMapper.getTaxCategories();
    }

    @Transactional
   public void deleteTaxCategories(List<TaxCategory> taxes) {
    for (TaxCategory tax : taxes) {
        taxCategoryMapper.deleteTaxCategory(tax);
        RecentActivity recentActivity = new RecentActivity();
        recentActivity.setMessage(tax.getType().trim() + " tax category is deleted");
        User user = new User();
        user.setId(tax.getUser_id());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }
}


}
