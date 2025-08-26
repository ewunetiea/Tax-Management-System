package com.afr.fms.Approver.Service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Approver.Mapper.AssetLiabilityApproverMapper;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Auditor.Mapper.UploadFileISMMapper;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.email.service.EmailService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class AssetLiabilityApproverservice {
   
       @Autowired
    private AuditISMMapper auditMapper;

    @Autowired

   private  AssetLiabilityApproverMapper assetLiabilityApproverMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChangeTrackerISMService changeTrackerISMService;
    private String baseURL = Endpoint.URL;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UploadFileISMMapper uploadFileISMMapper;
    @Autowired
    private RecentActivityMapper recentActivityMapper;

    private RecentActivity recentActivity;

    @Autowired
    private AuditeeDivisionISMMapper auditeeDivisionISMMapper;
    


       public List<AuditISM> getFindingBasedOnStatus(PaginatorPayLoad paginatorPayload) {

        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
    
        // Page<AuditISM> IsMgtPage;


                List<AuditISM> IsMgtAudit = new ArrayList<>();

    
        if (paginatorPayload.getAudit_status().equalsIgnoreCase("pending")) {
            IsMgtAudit = assetLiabilityApproverMapper.getPendingAudits(paginatorPayload);
        } else if (paginatorPayload.getAudit_status().equalsIgnoreCase("approved")) {
            IsMgtAudit = assetLiabilityApproverMapper.getApprovedAudits(paginatorPayload);
        }
        else if (paginatorPayload.getAudit_status().equalsIgnoreCase("rejected")) {
            IsMgtAudit = assetLiabilityApproverMapper.getRejectedFindings(paginatorPayload);
        }
         
        
        // else {
        //     // Optionally handle unknown status
        //     return new ArrayList<>(); // Or throw an exception
        // }
    
        // List<AuditISM> IsMgtAudit = IsMgtPage.getResult();
    
        // if (!IsMgtAudit.isEmpty()) {
        //     IsMgtAudit.get(0).setTotal_records_paginator(IsMgtPage.getTotal());
        // }
    
        return IsMgtAudit;
    }
}
