package com.afr.fms.Reviewer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Reviewer.Mapper.AbnormalBalanceReviewerMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class AbnormalBalanceReviewerService {
    @Autowired
    private AbnormalBalanceReviewerMapper abnormalBalanceReviewerMapper;

     public List<AuditISM> getPendingAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = abnormalBalanceReviewerMapper.getPendingAudits(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    
    public List<AuditISM> getReviewedAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = abnormalBalanceReviewerMapper.getReviewedAudits(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    public List<AuditISM> getRejectedAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = abnormalBalanceReviewerMapper.getRejectedAudits(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }
    
}
