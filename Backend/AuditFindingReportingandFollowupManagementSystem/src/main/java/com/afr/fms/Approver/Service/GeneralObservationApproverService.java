package com.afr.fms.Approver.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Approver.Mapper.GeneralObservationApproMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@Service
public class GeneralObservationApproverService {
    @Autowired
    private GeneralObservationApproMapper generalObservationMapper;

    public List<AuditISM> getPendingAudits(PaginatorPayLoad paginatorPayload) {
        List<AuditISM> inspectionAudits = generalObservationMapper.getPendingGeneralObservation(paginatorPayload);

        return inspectionAudits;
    }

    public List<AuditISM> getApprovedAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = generalObservationMapper.getApprovedGeneralObservation(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        // Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        // inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

    public List<AuditISM> getRejectedAudits(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = generalObservationMapper.getRejectedGeneralObservation(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        // Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        // inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return inspectionAudits;
    }

}
