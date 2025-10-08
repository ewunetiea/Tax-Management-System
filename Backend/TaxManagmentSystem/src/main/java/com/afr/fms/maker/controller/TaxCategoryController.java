package com.afr.fms.maker.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.afr.fms.Admin.Controller.BranchController;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.maker.entity.TaxCategory;
import com.afr.fms.maker.service.TaxCategoryService;


@RestController
@RequestMapping("/api/tax-category")
public class TaxCategoryController {

    @Autowired
    private TaxCategoryService taxCategoryService;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);

    @PostMapping("/create-edit")
    public ResponseEntity<HttpStatus> createEditTaxCategory(@RequestBody TaxCategory tax) {
        User user = new User();
        if (tax.getId() == null) {
            taxCategoryService.createTaxCategory(tax);
            recentActivity.setMessage(tax.getType() + " tax category is created ");
        } else {
            taxCategoryService.updateTaxCategory(tax);
            recentActivity.setMessage(tax.getType() + " tax category is updated ");
        }
        user.setId(tax.getUser_id());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<TaxCategory>> getTaxCategories(HttpServletRequest request) {
        List<TaxCategory> taxes = taxCategoryService.getTaxCategories();
        return new ResponseEntity<>(taxes, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteTaxCategory(@RequestBody List<TaxCategory> taxes, HttpServletRequest request) {
        try {
            User user = new User();
            for (TaxCategory tax : taxes) {
                taxCategoryService.deleteTaxCategory(tax);

                // Set user for recent activity (assuming getUser_id() returns valid id)
                recentActivity.setMessage(tax.getType() + " tax category is deleted ");
                user.setId(tax.getUser_id());
                recentActivity.setUser(user);

                // Add recent activity log
                recentActivityMapper.addRecentActivity(recentActivity);
            }

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            logger.error("Error while deleting tax category", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
