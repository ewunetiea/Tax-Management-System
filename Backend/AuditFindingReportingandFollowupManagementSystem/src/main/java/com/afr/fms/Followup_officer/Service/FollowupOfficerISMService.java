package com.afr.fms.Followup_officer.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Entity.AuditeeDivisionISM;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper;
import com.afr.fms.Auditee.Service.AuditeeISMService;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.Audit_Remark.RemarkService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Followup_officer.Mapper.FollowupOfficerISMMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class FollowupOfficerISMService {
    @Autowired
    private FollowupOfficerISMMapper auditMapper;

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

    @Autowired
    private AuditeeISMService auditeeISMService;

    RecentActivity recentActivity = new RecentActivity();

    private static final Logger logger = LoggerFactory.getLogger(FollowupOfficerISMService.class);

    public List<AuditISM> attachAuditeeResponse(List<AuditISM> auditISMs) {
        List<AuditISM> audits = new ArrayList<>();
        for (AuditISM auditISM : auditISMs) {
            List<IS_MGT_Auditee> is_MGT_Auditees = auditeeDivisionISMMapper.getISMAuditeesByAuditeeID(
                    auditISM.getDirectorate_id(),
                    auditISM.getId());

            auditISM.setIS_MGTAuditee(is_MGT_Auditees);
            audits.add(auditISM);
        }

        return audits;
    }

    public List<AuditISM> getAuditsForFollowupOfficer(AuditISM auditISM) {
        List<AuditISM> auditISMs = new ArrayList<>();
        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Long total_records;

        if (auditISM.getFollowup_officer().getCategory().equalsIgnoreCase("IS")) {
            Page<AuditISM> auditsPage = (Page<AuditISM>) auditMapper.getAuditsForFollowupOfficerIS(auditISM,
                    checkRequistionDates(auditISM.getFinding_dates(), 0) ? auditISM.getFinding_dates().get(0)
                            : null,
                    checkRequistionDates(auditISM.getFinding_dates(), 1) ? auditISM.getFinding_dates().get(1)
                            : null);

            auditISMs = auditsPage.getResult();
            total_records = auditsPage.getTotal(); // Get the total records from the page object
        } else {
            Page<AuditISM> auditsPage = (Page<AuditISM>) auditMapper.getAuditsForFollowupOfficerMGT(auditISM,
                    checkRequistionDates(auditISM.getFinding_dates(), 0) ? auditISM.getFinding_dates().get(0)
                            : null,
                    checkRequistionDates(auditISM.getFinding_dates(), 1) ? auditISM.getFinding_dates().get(1)
                            : null);

            auditISMs = auditsPage.getResult();
            total_records = auditsPage.getTotal(); // Get the total records from the page object

        }
        if (auditISMs.size() > 0) {
            auditISMs.get(0).setTotal_records(total_records);
        }
        return attachAuditeeResponse(auditISMs);
    }

    public List<AuditISM> getRectifiedFindings(AuditISM auditISM) {
        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Page<AuditISM> auditsPage = (Page<AuditISM>) (auditISM.getCategory().equals(
                "MGT") ? auditMapper.getRectifiedFindings(auditISM,
                        checkRequistionDates(auditISM.getFinding_dates(), 0) ? auditISM.getFinding_dates().get(0)
                                : null,
                        checkRequistionDates(auditISM.getFinding_dates(), 1) ? auditISM.getFinding_dates().get(1)
                                : null)
                        : auditMapper.getRectifiedISFindings(auditISM,
                                checkRequistionDates(auditISM.getFinding_dates(), 0)
                                        ? auditISM.getFinding_dates().get(0)
                                        : null,
                                checkRequistionDates(auditISM.getFinding_dates(), 1)
                                        ? auditISM.getFinding_dates().get(1)
                                        : null));

        List<AuditISM> auditISMs = auditsPage.getResult(); // Get the actual list of audits from the page object
        if (auditISMs.size() > 0) {
            auditISMs.get(0).setTotal_records(auditsPage.getTotal());
        }
        return attachAuditeeResponse(auditISMs);
    }

    public List<AuditISM> getUnrectifiedFindings(AuditISM auditISM) {

        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Page<AuditISM> auditsPage = (Page<AuditISM>) auditMapper.getUnrectifiedFindings(auditISM,
                checkRequistionDates(auditISM.getFinding_dates(), 0) ? auditISM.getFinding_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getFinding_dates(), 1) ? auditISM.getFinding_dates().get(1)
                        : null);

        List<AuditISM> auditISMs = auditsPage.getResult(); // Get the actual list of audits from the page object
        if (auditISMs.size() > 0) {
            auditISMs.get(0).setTotal_records(auditsPage.getTotal());
        }
        return attachAuditeeResponse(auditISMs);
    }

    public List<AuditISM> getRejectedFindings(Long followup_officer_id) {
        List<AuditISM> auditISMs = auditMapper.getRejectedFindings(followup_officer_id);
        return attachAuditeeResponse(auditISMs);
    }

    public List<AuditISM> getPartiallyRectifiedFindings(AuditISM auditISM) {
        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Page<AuditISM> auditsPage = (Page<AuditISM>) auditMapper.getPartiallyRectifiedFindings(auditISM,
                checkRequistionDates(auditISM.getFinding_dates(), 0) ? auditISM.getFinding_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getFinding_dates(), 1) ? auditISM.getFinding_dates().get(1)
                        : null);

        List<AuditISM> auditISMs = auditsPage.getResult(); // Get the actual list of audits from the page object
        if (auditISMs.size() > 0) {
            auditISMs.get(0).setTotal_records(auditsPage.getTotal());
        }
        return attachAuditeeResponse(auditISMs);
    }

    public boolean checkRequistionDates(List<String> finding_dates, int index) {
        if (finding_dates != null) {
            if (index == 0)
                return finding_dates.size() > 0;
            return finding_dates.size() > 1;
        }
        return false;
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

    public void partiallyRectifyISMFindings(Remark remark) {
        remark.setRejected(false);
        List<AuditISM> auditISMs = remark.getAudits();
        User followupUser = auditISMs.get(0).getFollowup_officer();
        for (AuditISM auditISM : auditISMs) {
            auditISM.setFollowup_officer(followupUser);
            remark.setAudit(auditISM);
            for (IS_MGT_Auditee is_MGT_Auditee : auditISM.getIS_MGTAuditee()) {
                if ((is_MGT_Auditee.getComplete_status() && remark.getStarting_url().contains("Rectified"))
                        || (is_MGT_Auditee.getRectification_status() == 2
                                && remark.getStarting_url().contains("Unrectified"))
                        || (is_MGT_Auditee.getRectification_status() == 4
                                && remark.getStarting_url().contains("Partially"))) {
                    try {
                        List<User> recievers = userMapper.getUserByBranchandRole(is_MGT_Auditee.getAuditee_id(),
                                "AUDITEE");
                        for (User reciever : recievers) {
                            remark.setReciever(reciever);
                            remarkService.addRemark(remark);
                        }
                    } catch (Exception e) {
                        logger.error("Error while sending remark to auditee", e);
                    }
                }
                for (AuditeeDivisionISM auditeeDivisionISM : is_MGT_Auditee.getAuditeeDivisionISM()) {
                    if (auditeeDivisionISM.getSubmitted_auditee() && auditeeDivisionISM.getDivision() != null) {
                        try {
                            List<User> recieversDivision = userMapper
                                    .getUserByBranchandRole(auditeeDivisionISM.getDivision().getId(),
                                            "AUDITEE_DIVISION");
                            for (User reciever : recieversDivision) {
                                remark.setReciever(reciever);
                                remarkService.addRemark(remark);
                            }
                        } catch (Exception e) {
                            logger.error("Error while sending remark to auditee division", e);
                        }
                    }
                }

            }

            auditMapper.partiallyRectifyISMFindings(auditISM);
            auditMapper.partiallyRectifyFindingsAuditee(auditISM.getIS_MGTAuditee().get(0));

            updateUnrectifyRelatedAuditStatus(auditISM);

            recentActivity.setMessage(" Finding " + auditISM.getCase_number() + " is partially rectified");
            recentActivity.setUser(followupUser);
            recentActivityMapper.addRecentActivity(recentActivity);
        }

    }

    // public void partiallyRectifyISMFindings(AuditISM audit) {

    // auditMapper.partiallyRectifyISMFindings(audit);
    // auditMapper.partiallyRectifyFindingsAuditee(audit.getIS_MGTAuditee().get(0));

    // updateUnrectifyRelatedAuditStatus(audit);

    // recentActivity.setMessage(" Finding " + audit.getCase_number() + " is
    // partially rectified");
    // recentActivity.setUser(audit.getFollowup_officer());
    // recentActivityMapper.addRecentActivity(recentActivity);
    // }

    public void rejectISMFinding(Remark remark) {
        remark.setRejected(true);
        AuditISM auditISM = auditISMMapper.getAudit(remark.getAudit().getId());
        for (IS_MGT_Auditee is_MGT_Auditee : auditISM.getIS_MGTAuditee()) {
            if ((is_MGT_Auditee.getComplete_status() && remark.getStarting_url().contains("Rectified"))
                    || (is_MGT_Auditee.getRectification_status() == 2
                            && remark.getStarting_url().contains("Unrectified"))
                    || (is_MGT_Auditee.getRectification_status() == 4
                            && remark.getStarting_url().contains("Partially"))) {
                try {
                    List<User> recievers = userMapper.getUserByBranchandRole(is_MGT_Auditee.getAuditee_id(), "AUDITEE");
                    for (User reciever : recievers) {
                        remark.setReciever(reciever);
                        remarkService.addRemark(remark);
                    }
                } catch (Exception e) {
                    logger.error("Error while sending remark to auditee", e);
                }
            }
            for (AuditeeDivisionISM auditeeDivisionISM : is_MGT_Auditee.getAuditeeDivisionISM()) {
                if (auditeeDivisionISM.getSubmitted_auditee()) {
                    try {
                        List<User> recieversDivision = userMapper
                                .getUserByBranchandRole(auditeeDivisionISM.getDivision().getId(), "AUDITEE_DIVISION");
                        for (User reciever : recieversDivision) {
                            remark.setReciever(reciever);
                            remarkService.addRemark(remark);
                        }

                    } catch (Exception e) {
                        logger.error("Error while sending remark to auditee division", e);
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

                if (auditeeDivisionISM.getAuditee_submitted() == 1) {
                    auditeeISMService.updatePreviousResponse(auditeeDivisionISM);
                }
                auditMapper.updateAuditeeDivision(is_MGT_Auditee.getSelf_response(), auditeeDivisionISM);
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