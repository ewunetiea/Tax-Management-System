package com.afr.fms.Followup_officer.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.Audit_Remark.RemarkService;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Followup_officer.Mapper.FollowupOfficerMGTEnhancedMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class FollowupOfficerMGTEnhancedService {
    @Autowired
    private FollowupOfficerMGTEnhancedMapper auditMapper;

    @Autowired
    private AuditISMMapper auditISMMapper;

    @Autowired
    private RemarkService remarkService;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuditeeDivisionISMMapper auditeeDivisionISMMapper;

    RecentActivity recentActivity = new RecentActivity();

    public List<AuditISM> attachAuditeeResponse(List<AuditISM> auditISMs) {
        List<AuditISM> audits = new ArrayList<>();
        for (AuditISM auditISM : auditISMs) {
            List<IS_MGT_Auditee> is_MGT_Auditees = auditeeDivisionISMMapper
                    .getISMAuditeesByAuditeeID(auditISM.getDirectorate_id(), auditISM.getId());
            auditISM.setIS_MGTAuditee(is_MGT_Auditees);
            audits.add(auditISM);
        }
        return audits;
    }

    public List<AuditISM> getAuditsForFollowupOfficer(PaginatorPayLoad paginatorPayLoad) {
        // PageHelper.startPage(paginatorPayLoad.getCurrentPage(), paginatorPayLoad.getPageSize());
        List<AuditISM> auditISMs = new ArrayList<>();
        if (paginatorPayLoad.getCategory().equalsIgnoreCase("IS")) {
            auditISMs = auditMapper.getAuditsForFollowupOfficerIS(paginatorPayLoad);
        } else {
            auditISMs = auditMapper.getAuditsForFollowupOfficerMGT(paginatorPayLoad);
            // if (!auditISMs.isEmpty()) {
            //     Page<AuditISM> page = (Page<AuditISM>) auditISMs;
            //     auditISMs.get(0).setTotal_records_paginator(page.getTotal());
            // }
        }
        return attachAuditeeResponse(auditISMs);
    }

    // public List<AuditISM> getRectifiedFindings(PaginatorPayLoad paginatorPayLoad)
    // {
    // List<AuditISM> auditISMs =
    // auditMapper.getRectifiedFindings(paginatorPayLoad);
    // return attachAuditeeResponse(auditISMs);
    // }

    public List<AuditISM> getRectifiedFindings(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = auditMapper.getRectifiedFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return attachAuditeeResponse(inspectionAudits);
    }

    // public List<AuditISM> getUnrectifiedFindings(PaginatorPayLoad
    // paginatorPayLoad) {
    // List<AuditISM> auditISMs =
    // auditMapper.getUnrectifiedFindings(paginatorPayLoad);
    // return attachAuditeeResponse(auditISMs);
    // }

    public List<AuditISM> getUnrectifiedFindings(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = auditMapper.getUnrectifiedFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return attachAuditeeResponse(inspectionAudits);
    }

    public List<AuditISM> getRejectedFindings(PaginatorPayLoad paginatorPayLoad) {
        List<AuditISM> auditISMs = auditMapper.getRejectedFindings(paginatorPayLoad);
        return attachAuditeeResponse(auditISMs);
    }

    // public List<AuditISM> getPartiallyRectifiedFindings(PaginatorPayLoad
    // paginatorPayLoad) {
    // List<AuditISM> auditISMs =
    // auditMapper.getPartiallyRectifiedFindings(paginatorPayLoad);
    // return attachAuditeeResponse(auditISMs);
    // }

    public List<AuditISM> getPartiallyRectifiedFindings(PaginatorPayLoad paginatorPayload) {
        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());
        List<AuditISM> inspectionAudits = auditMapper.getPartiallyRectifiedFindings(paginatorPayload);
        // if (!inspectionAudits.isEmpty()) {
        //     Page<AuditISM> page = (Page<AuditISM>) inspectionAudits;
        //     inspectionAudits.get(0).setTotal_records_paginator(page.getTotal());
        // }
        return attachAuditeeResponse(inspectionAudits);
    }

    public void rectifyISMFindings(AuditISM audit) {

        auditMapper.rectifyISMFindings(audit);
        auditMapper.rectifyFindingsAuditee(audit.getIS_MGTAuditee().get(0));

        recentActivity.setMessage(" Finding " + audit.getCase_number() + " is rectified");
        recentActivity.setUser(audit.getFollowup_officer());
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public void unRectifyISMFindings(AuditISM audit) {
        auditMapper.unRectifyISMFindings(audit);
        recentActivity.setMessage(" Finding " + audit.getCase_number() + " is unrectified");
        recentActivity.setUser(audit.getFollowup_officer());
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public void partiallyRectifyISMFindings(AuditISM audit) {
        auditMapper.partiallyRectifyISMFindings(audit);
        auditMapper.partiallyRectifyFindingsAuditee(audit.getIS_MGTAuditee().get(0));

        updateUnrectifyRelatedAuditStatus(audit);

        recentActivity.setMessage(" Finding " + audit.getCase_number() + " is partially rectified");
        recentActivity.setUser(audit.getFollowup_officer());
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public void rejectISMFinding(Remark remark) {

        remark.setRejected(true);
        AuditISM auditISM = auditISMMapper.getAudit(remark.getAudit().getId());
        for (IS_MGT_Auditee is_MGT_Auditee : auditISM.getIS_MGTAuditee()) {
            if (is_MGT_Auditee.getComplete_status()) {
                try {
                    // User reciever =
                    // userMapper.getUserByBranchandRole(is_MGT_Auditee.getAuditee_id(),
                    // "AUDITEE").get(0);
                    List<User> recievers = userMapper.getUserByBranchandRole(is_MGT_Auditee.getAuditee_id(), "AUDITEE");
                    for (User reciever : recievers) {
                        remark.setReciever(reciever);
                        remarkService.addRemark(remark);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            for (AuditeeDivisionISM auditeeDivisionISM : is_MGT_Auditee.getAuditeeDivisionISM()) {

                if (auditeeDivisionISM.getSubmitted_auditee()) {
                    try {
                        // User reciever = userMapper
                        // .getUserByBranchandRole(auditeeDivisionISM.getDivision().getId(),
                        // "AUDITEE_DIVISION")
                        // .get(0);

                        List<User> recieversDivision = userMapper
                                .getUserByBranchandRole(auditeeDivisionISM.getDivision().getId(), "AUDITEE_DIVISION");
                        for (User reciever : recieversDivision) {
                            remark.setReciever(reciever);
                            remarkService.addRemark(remark);
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }

            auditMapper.rejectISMFinding(remark.getAudit());
            auditMapper.unrectifyFindingAuditee(remark.getAudit().getIS_MGTAuditee().get(0));

            updateUnrectifyRelatedAuditStatus(remark.getAudit());
        }

        recentActivity.setMessage(" Finding " + remark.getAudit().getCase_number() + " is unrectified");
        recentActivity.setUser(remark.getAudit().getFollowup_officer());
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public void updateUnrectifyRelatedAuditStatus(AuditISM audit) {
        AuditISM auditISM = auditISMMapper.getAudit(audit.getId());
        for (IS_MGT_Auditee is_MGT_Auditee : auditISM.getIS_MGTAuditee()) {
            auditMapper.updateIS_MGT_Auditee(is_MGT_Auditee.getId());

            for (AuditeeDivisionISM auditeeDivisionISM : is_MGT_Auditee.getAuditeeDivisionISM()) {
                auditMapper.updateAuditeeDivision(auditeeDivisionISM.getId());
            }
        }
    }

    public void rectifyMultipleAudits(List<AuditISM> audits) {
        User followupUser = audits.get(0).getFollowup_officer();
        for (AuditISM auditISM : audits) {
            auditISM.setFollowup_officer(followupUser);
            auditMapper.rectifyISMFindings(auditISM);
            auditMapper.rectifyFindingsAuditee(auditISM.getIS_MGTAuditee().get(0));

            recentActivity.setMessage(" Finding " + auditISM.getCase_number() + " is rectified");
            recentActivity.setUser(auditISM.getFollowup_officer());
            recentActivityMapper.addRecentActivity(recentActivity);
        }
    }

    public void unRectifySelectedISMFindings(List<AuditISM> audits) {

        User followupUser = audits.get(0).getFollowup_officer();
        for (AuditISM auditISM : audits) {
            auditISM.setFollowup_officer(followupUser);
            auditMapper.unRectifyISMFindings(auditISM);

            recentActivity.setMessage(" Finding " + auditISM.getCase_number() + " is unrectifed");
            recentActivity.setUser(auditISM.getFollowup_officer());
            recentActivityMapper.addRecentActivity(recentActivity);
        }

    }

}