package com.afr.fms.Auditor.Service;

import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.afr.fms.Admin.Entity.Branch;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Auditor.Mapper.CommonActionAuditorMapper;
import com.afr.fms.Auditor.Mapper.SuspenseAccountAuditorMapper;
import com.afr.fms.Auditor.Mapper.UploadFileISMMapper;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMService;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.Change_Tracker_ISM;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.email.context.Auditor_for_ReviewerContext;
import com.afr.fms.Security.email.service.EmailService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class SuspenseAccountService {

    @Autowired
    private AuditISMMapper auditMapper;

    @Autowired

    private SuspenseAccountAuditorMapper suspenseAccountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChangeTrackerISMService changeTrackerISMService;

    private String baseURL = Endpoint.URL;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UploadFileISMMapper uploadFileISMMapper;
    @Autowired
    private RecentActivityMapper recentActivityMapper;

    private RecentActivity recentActivity;

    @Autowired
    private AuditeeDivisionISMMapper auditeeDivisionISMMapper;

    @Autowired

    private CommonActionAuditorMapper commonActionAuditorMapper;

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

    public List<AuditISM> getFindingBasedOnStatus(PaginatorPayLoad paginatorPayload) {

        // PageHelper.startPage(paginatorPayload.getCurrentPage(), paginatorPayload.getPageSize());

        // Page<AuditISM> IsMgtPage;

                List<AuditISM> IsMgtAudit = new ArrayList<>();
 

        if (paginatorPayload.getAudit_status().equalsIgnoreCase("drafted")) {
            IsMgtAudit =  suspenseAccountMapper.getDraftedSuspenseAccount(paginatorPayload);
        } else if (paginatorPayload.getAudit_status().equalsIgnoreCase("passed")) {
             IsMgtAudit = suspenseAccountMapper.getPassedSuspenseAccount(paginatorPayload);
        }

        else if (paginatorPayload.getAudit_status().equalsIgnoreCase("rejected")) {

            IsMgtAudit = suspenseAccountMapper.getRejectedFindings(paginatorPayload);
        }

       

        // List<AuditISM> IsMgtAudit = IsMgtPage.getResult();

        // if (!IsMgtAudit.isEmpty()) {
        //     IsMgtAudit.get(0).setTotal_records_paginator(IsMgtPage.getTotal());
        // }

        return IsMgtAudit;
    }

    public List<AuditISM> getAuditsOnProgressForAuditor(Long auditor_id) {
        List<AuditISM> auditISMs = auditMapper.getAuditsOnProgressForAuditor(auditor_id);
        return attachAuditeeResponse(auditISMs);
    }

    public void createSuspenseAccount(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getAuditor().getId());
        audit.setCategory(user.getCategory());
        audit.setCase_number(generateCaseNumber(audit.getCategory()));
        Long audit_id = (long) 0;

        audit_id = auditMapper.createMGTFindingEnhanced(audit);
        audit.getSuspenseAccount().setIs_mgt_audit_id(audit_id);
        suspenseAccountMapper.createSuspenseAccount(audit.getSuspenseAccount());
System.out.println("__________________________________FF__________________");


System.out.println(audit.getSuspenseAccount());
       

        IS_MGT_Auditee IS_MGTAuditee = new IS_MGT_Auditee();
        IS_MGTAuditee.setAuditISM_id(audit_id);
        for (Branch auditee : audit.getAuditees()) {
            IS_MGTAuditee.setAuditee_id(auditee.getId());
            auditMapper.createISMAuditee(IS_MGTAuditee);
        }

        if (audit.getEdit_auditee()) {
            for (String file_name : audit.getFile_urls()) {
                uploadFileISMMapper.InsertFileUrl(file_name, audit_id);
            }
        }

        for (Change_Tracker_ISM change_Tracker_ISM : audit.getChange_tracker_ISM()) {
            // if (!change_Tracker_ISM.equals(new Change_Tracker_ISM())) {
            if (change_Tracker_ISM != null) {
                change_Tracker_ISM.setAudit_id(audit_id);
                change_Tracker_ISM.setChanger(audit.getEditor());
                changeTrackerISMService.insertChanges(change_Tracker_ISM);
            }
        }

        recentActivity = new RecentActivity();
        recentActivity.setMessage(" Finding with case number  " + audit.getCase_number() + " is added.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void updateSuspenseAccount(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getAuditor().getId());
        System.out.println(audit.getAuditees());
        if (audit.getCategory() == "IS") {
            commonActionAuditorMapper.updateISFinding(audit);
        } else if (audit.getFinding_detail() == null) {
            auditMapper.updateMGTFinding(audit);
            suspenseAccountMapper.updateSuspenseAccount(audit.getSuspenseAccount());

        } else {
            auditMapper.updateMGTFindingandFindingDetail(audit);
            suspenseAccountMapper.updateSuspenseAccount(audit.getSuspenseAccount());
        }
        if (audit.getEdit_auditee()) {
            commonActionAuditorMapper.deleteISMAuditee(audit.getId());
            IS_MGT_Auditee IS_MGTAuditee = new IS_MGT_Auditee();
            IS_MGTAuditee.setAuditISM_id(audit.getId());

            if (audit.getEdit_auditee()) {
                for (Branch auditee : audit.getAuditees()) {
                    IS_MGTAuditee.setAuditee_id(auditee.getId());
                    auditMapper.createISMAuditee(IS_MGTAuditee);
                }
                uploadFileISMMapper.removeFileUrls(audit.getId());
                for (String file_name : audit.getFile_urls()) {
                    uploadFileISMMapper.InsertFileUrl(file_name, audit.getId());
                }
            }

        }

        for (Change_Tracker_ISM change_Tracker_ISM : audit.getChange_tracker_ISM()) {
            if (change_Tracker_ISM != null) {
                change_Tracker_ISM.setAudit_id(audit.getId());
                change_Tracker_ISM.setChanger(audit.getEditor());
                changeTrackerISMService.insertChanges(change_Tracker_ISM);
            }
        }

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Audit with case number  " + audit.getCase_number() + " is updated.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public String generateCaseNumber(String category) {
        int case_number_number = 1;
        String last_case_number = auditMapper.getLatestCaseNumber();
        if (last_case_number != null) {
            case_number_number = Integer.parseInt(last_case_number.replaceAll("[^0-9]", ""));
        }
        String case_number = category + (case_number_number + 1);
        return case_number;
    }

    public void changeRecitificationStatus(AuditISM auditISM) {
        commonActionAuditorMapper.changeRecitificationStatus(auditISM);
    }

    public void sendEmailtoReviewer(AuditISM audit) {
        Auditor_for_ReviewerContext emailContext = new Auditor_for_ReviewerContext();
        emailContext.init(audit);
        emailContext.buildReviewRequestUrl(baseURL);
        try {
            emailService.sendMail(emailContext);
            System.out.println("Audit Review Request is sent");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public List<AuditISM> getAuditFindings() {
        return auditMapper.getAuditFindings();

    }

}
