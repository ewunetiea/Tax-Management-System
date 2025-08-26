package com.afr.fms.Auditee.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditee.Mapper.AuditeeISMEnhancedMapper;
import com.afr.fms.Auditee.Mapper.AuditeeISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Common.Entity.PaginatorPayLoad;

@Service
public class AuditeeEnhancedService {
    @Autowired
    private AuditeeISMEnhancedMapper auditDivisionISMMapper;

    @Autowired
    private AuditeeISMMapper auditeeISMMapper;

    @Autowired
    private UserMapper userMapper;

    public List<AuditeeDivisionISM> getAuditeeResponses(Long IS_MGT_Auditee_id) {
        return auditDivisionISMMapper.getAuditeeResponse(IS_MGT_Auditee_id);
    }

    public AuditeeDivisionISM getAuditeeResponseISM(Long auditee_division_id) {
        return auditDivisionISMMapper.getAuditeeResponseISM(auditee_division_id);
    }

    public List<AuditISM> getAuditsForAuditee(PaginatorPayLoad paginatorPayload) {
        User user = userMapper.getAuditorById(paginatorPayload.getUser_id());
        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());
        List<AuditISM> auditISMs = auditDivisionISMMapper.getAuditsForAuditee(user.getBranch().getId(),
                paginatorPayload.getAudit_type());

        // if (!auditISMs.isEmpty()) {
        // Page<AuditISM> page = (Page<AuditISM>) auditISMs;
        // auditISMs.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> attachAuditeeResponse(List<AuditISM> auditISMs, Long auditee_id) {
        List<AuditISM> audits = new ArrayList<>();
        for (AuditISM auditISM : auditISMs) {
            List<Branch> assignedDivisions = new ArrayList<>();
            List<IS_MGT_Auditee> is_MGT_Auditees = auditeeISMMapper.getISMAuditeesByID(auditISM.getAuditee_id());
            List<AuditeeDivisionISM> assignedDivisionsInfo = auditeeISMMapper
                    .getAuditeeResponseById(auditISM.getDivision_auditee_id());

            assignedDivisions = getAssignedDivisions(is_MGT_Auditees.get(0).getId());
            is_MGT_Auditees.get(0).setDivisions(assignedDivisions);

            is_MGT_Auditees.get(0).setAuditeeDivisionISM(assignedDivisionsInfo);

            is_MGT_Auditees.get(0).setAssignedAuditeeDivisionISM(assignedDivisionsInfo);
            auditISM.setIS_MGTAuditee(is_MGT_Auditees);
            audits.add(auditISM);
        }
        return audits;
    }

    public List<Branch> getAssignedDivisions(Long auditee_id) {
        List<AuditeeDivisionISM> assignedAuditeeDivisions = auditDivisionISMMapper
                .getAssignedAuditeeDivisions(auditee_id);
        List<Branch> divisions = assignedAuditeeDivisions.stream()
                .map(AuditeeDivisionISM::getDivision)
                .collect(Collectors.toList());
        return divisions;
    }

    public List<AuditISM> getPartiallyRectifiedAuditsForAuditee(PaginatorPayLoad paginatorPayload) {
        User user = userMapper.getAuditorById(paginatorPayload.getUser_id());
        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());
        List<AuditISM> auditISMs = auditDivisionISMMapper
                .getPartiallyRectifiedAuditsForAuditee(user.getBranch().getId(), paginatorPayload.getAudit_type());
        // if (!auditISMs.isEmpty()) {
        // Page<AuditISM> page = (Page<AuditISM>) auditISMs;
        // auditISMs.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getAuditsForAuditeeOnProgress(PaginatorPayLoad paginatorPayload) {
        User user = userMapper.getAuditorById(paginatorPayload.getUser_id());
        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());
        List<AuditISM> auditISMs = auditDivisionISMMapper.getAuditsForAuditeeOnProgress(user.getBranch().getId(),
                paginatorPayload.getAudit_type());
        // if (!auditISMs.isEmpty()) {
        // Page<AuditISM> page = (Page<AuditISM>) auditISMs;
        // auditISMs.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getUnrectifiedAuditsForAuditee(PaginatorPayLoad paginatorPayload) {
        User user = userMapper.getAuditorById(paginatorPayload.getUser_id());
        // PageHelper.startPage(paginatorPayload.getCurrentPage(),
        // paginatorPayload.getPageSize());
        List<AuditISM> auditISMs = auditDivisionISMMapper.getUnrectifiedAuditsForAuditee(user.getBranch().getId(),
                paginatorPayload.getAudit_type());
        // if (!auditISMs.isEmpty()) {
        // Page<AuditISM> page = (Page<AuditISM>) auditISMs;
        // auditISMs.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

    public List<AuditISM> getRespondedAuditsForAuditee(Long auditee_id) {
        User user = userMapper.getAuditorById(auditee_id);
        List<AuditISM> auditISMs = auditDivisionISMMapper.getRespondedAuditsForAuditee(user.getBranch().getId());
        return attachAuditeeResponse(auditISMs, user.getBranch().getId());
    }

}
