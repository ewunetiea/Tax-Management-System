package com.afr.fms.Reviewer.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Reviewer.Mapper.MemorandomContingentReviewerMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class MemorandomReviewerService {
   
    @Autowired

   private  MemorandomContingentReviewerMapper memorandomReviewerMapper;

  
       public List<AuditISM> getFindingBasedOnStatus(PaginatorPayLoad paginatorPayload) {

        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
    
        // Page<AuditISM> IsMgtPage;
    
          List<AuditISM> IsMgtAudit  = new ArrayList<>();
        if (paginatorPayload.getAudit_status().equalsIgnoreCase("pending")) {
           IsMgtAudit  = memorandomReviewerMapper.getPendingAudits(paginatorPayload);
        } else if (paginatorPayload.getAudit_status().equalsIgnoreCase("reviewed")) {
            
           IsMgtAudit  = memorandomReviewerMapper.getReviewedAudits(paginatorPayload);
        }
        else if (paginatorPayload.getAudit_status().equalsIgnoreCase("rejected")) {
           IsMgtAudit  = memorandomReviewerMapper.getApproverRejectedFindings(paginatorPayload);
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
