package com.afr.fms.Reviewer.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class CreditDocumentationReviewerService {

    @Autowired

    private CreditDocumentationNewMapper creditDocumentationNewMapper;

    public List<AuditISM> getFindingBasedOnStatus(PaginatorPayLoad paginatorPayload) {

        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());

        List<AuditISM> IsMgtAudit = new ArrayList<>();

        IsMgtAudit = creditDocumentationNewMapper
                .getFindingBasedOnStatus(paginatorPayload.getUser_id(), paginatorPayload.getAudit_status(),
                        paginatorPayload.getCategory(), paginatorPayload.getBanking());

        // List<AuditISM> auditISM = inspeactionPage.getResult();

        // if (!auditISM.isEmpty()) {
        // auditISM.get(0).setTotal_records_paginator(inspeactionPage.getTotal());
        // }

        return IsMgtAudit;
    }

}
