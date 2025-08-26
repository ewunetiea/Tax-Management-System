package com.afr.fms.Reviewer.Service;

import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Reviewer.Mapper.CashCountReviewerMapper;

@Service
public class CashCountReviewerService {

    @Autowired
    private CashCountReviewerMapper cashCountReviewerMapper;

     public List<AuditISM> getPendingAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = cashCountReviewerMapper.getPendingFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    
    public List<AuditISM> getReviewedAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = cashCountReviewerMapper.getReviewedFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    public List<AuditISM> getRejectedAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = cashCountReviewerMapper.getRejectedFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }
    
}
