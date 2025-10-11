package com.afr.fms.HO.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.HO.mapper.ManageTaxHoMapper;
import com.afr.fms.Maker.entity.Tax;

@Service
public class ManageTaxHoService {
    @Autowired
    private ManageTaxHoMapper manageTaxHoMapper;

     @Autowired
    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();
    
    @Transactional
    public void approveTaxes(List<Tax> taxes) {
        for (Tax tax : taxes) {
            manageTaxHoMapper.approveTaxes(tax);

            User user = new User();
            RecentActivity recentActivity = new RecentActivity();
            recentActivity.setMessage("Tax with reference number " + tax.getReference_number().trim() + " has been approved ");
            user.setId(tax.getUser_id());
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);
        }
    }

     @Transactional
    public void rejectTax(Tax tax) {
        manageTaxHoMapper.rejectTax(tax);
        
        User user = new User();
        RecentActivity recentActivity = new RecentActivity();
        recentActivity.setMessage("Tax with reference number " + tax.getReference_number().trim() + " has been rejected ");
        user.setId(tax.getUser_id());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

}
