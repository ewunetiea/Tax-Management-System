package com.afr.fms.Reviewer.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Reviewer.Mapper.ManageTaxReviewerMapper;

@Service
public class ManageTaxReviewerService {

    @Autowired
    private ManageTaxReviewerMapper manageTaxMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    public List<Tax> getPendingTaxes(PaginatorPayLoad paginatorPayLoad) {
        return manageTaxMapper.getPendingTaxes(paginatorPayLoad);
    }

    public List<Tax> getRejectedTaxes(PaginatorPayLoad paginatorPayLoad) {
        return manageTaxMapper.getRejectedTaxes(paginatorPayLoad);
    }

    public List<Tax> getApprovedTaxes(PaginatorPayLoad paginatorPayLoad) {
        return manageTaxMapper.getApprovedTaxes(paginatorPayLoad);
    }

    @Transactional
    public void reviewTaxes(List<Tax> taxes) {
        for (Tax tax : taxes) {
            manageTaxMapper.reviewTaxes(tax);

            User user = new User();
            RecentActivity recentActivity = new RecentActivity();
            recentActivity.setMessage("Tax with reference number " + tax.getReference_number().trim() + " has been reviewed ");
            user.setId(tax.getUser_id());
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);
        }
    }

    @Transactional
    public void rejectTax(Tax tax) {
        manageTaxMapper.rejectTax(tax);
        
        User user = new User();
        RecentActivity recentActivity = new RecentActivity();
        recentActivity.setMessage("Tax with reference number " + tax.getReference_number().trim() + " has been rejected ");
        user.setId(tax.getUser_id());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

}
