package com.afr.fms.Reviewer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Reviewer.Mapper.GeneeralObservationRevieweMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.List;

@Service
public class GeneeralObservationRevieweService {
    
    @Autowired
    private GeneeralObservationRevieweMapper geneeralObservationRevieweMapper;

     public List<AuditISM> getPendingGeneralObservation(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = geneeralObservationRevieweMapper.getPendingGeneralObservation(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    
    public List<AuditISM> getReviewedGeneralObservation(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = geneeralObservationRevieweMapper.getReviewedGeneralObservation(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    public List<AuditISM> getRejectedGeneralObservation(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = geneeralObservationRevieweMapper.getRejectedGeneralObservation(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    
    
}
