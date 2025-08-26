package com.afr.fms.Approver.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.afr.fms.Approver.Mapper.CashCountApproverMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import java.util.List;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class CashCountApproverService {
    @Autowired
    private CashCountApproverMapper cashCountApproverMapper;

     public List<AuditISM> getPendingAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = cashCountApproverMapper.getPendingFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    public List<AuditISM> getApprovedAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = cashCountApproverMapper.getApprovedFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }


    public List<AuditISM> getRejectedAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = cashCountApproverMapper.getRejectedFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }
    
}
