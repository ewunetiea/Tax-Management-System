package com.afr.fms.Auditor.Service;

import java.util.List;
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
import com.afr.fms.Auditor.Mapper.CreditDocumentationNewMapper;
import com.afr.fms.Auditor.Mapper.UploadFileISMMapper;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMService;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.Change_Tracker_ISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Model.MGT.CreditDocumentationChild;
import com.afr.fms.Model.MGT.CreditDocumentationParent;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.email.service.EmailService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class CreditDocumentationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuditISMMapper auditMapper;

    @Autowired

    private CreditDocumentationNewMapper creditDocumentationNewMapper;

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

    public Long createCreditDocumentationParent(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getAuditor().getId());
        audit.setCategory(user.getCategory());
        audit.setCase_number(generateCaseNumber(audit.getCategory()));

        Long audit_id = (long) 0;
        Long loan_id = (long) 0;

        audit_id = auditMapper.createMGTFindingEnhanced(audit);
        audit.getCreditDocumentationParent().setIs_mgt_audit_id(audit_id);
        loan_id = creditDocumentationNewMapper.createCreditDocumentationParent(audit.getCreditDocumentationParent());

        CreditDocumentationParent loanAdvance = audit.getCreditDocumentationParent();
        loanAdvance.setIs_mgt_audit_id(audit_id);
        loanAdvance.setBranch(user.getBranch());

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

        return loan_id;

    }

    public void updateAmendedLoanAndAdvanceParent(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getAuditor().getId());

        if (audit.getFinding_detail() == null) {
            auditMapper.updateMGTFinding(audit);
            creditDocumentationNewMapper.updateCreditDocumentationParent(audit.getCreditDocumentationParent());
        } else {
            auditMapper.updateMGTFindingandFindingDetail(audit);
            creditDocumentationNewMapper.updateCreditDocumentationParent(audit.getCreditDocumentationParent());
        }

        // CreditDocumentationParent loanAndAdvance =
        // audit.getCreditDocumentationParent();
        // loanAndAdvance.setInspection_audit_id(audit.getId());
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

    public void createCreditDocumentationChild(CreditDocumentationChild updated) {
        creditDocumentationNewMapper.createCreditDocumentationChild(updated);
    }

    public void updateDynamicLoanAdvance(CreditDocumentationChild commonLoan) {
        creditDocumentationNewMapper.updateCreditDocumentationChild(commonLoan);

    }

    // public List<AuditISM> getAmendedLoanAndAdvance(User user) {

    // return creditDocumentationNewMapper.getAmendedLoanAndAdvance(user.getId(),
    // user.getCategory());
    // }

    public List<AuditISM> getFindingBasedOnStatus(PaginatorPayLoad paginatorPayLoad) {
        // PageHelper.startPage(paginatorPayLoad.getCurrentPage(), paginatorPayLoad.getPageSize());

        // Page<AuditISM> inspeactionPage = (Page<AuditISM>) creditDocumentationNewMapper
        //         .getFindingBasedOnStatus(paginatorPayLoad.getUser_id(), paginatorPayLoad.getAudit_status(),
        //                 paginatorPayLoad.getCategory(), paginatorPayLoad.getBanking());

        // List<AuditISM> auditISM = inspeactionPage.getResult();

    

        // if (!auditISM.isEmpty()) {
        //     auditISM.get(0).setTotal_records_paginator(inspeactionPage.getTotal());
        // }

           List<AuditISM> auditISM =   creditDocumentationNewMapper
                .getFindingBasedOnStatus(paginatorPayLoad.getUser_id(), paginatorPayLoad.getAudit_status(),
                        paginatorPayLoad.getCategory(), paginatorPayLoad.getBanking());

        return auditISM;
    }

    public CreditDocumentationParent getAmendedLoanAndAdvanceById(Long audit_id) {

        return creditDocumentationNewMapper.getCreditDocumentationParentByISMGTID(audit_id);
    }

    public boolean checkAccountNumberExist(String account_number) {

        return creditDocumentationNewMapper.getAccountNumberExistance(account_number);
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

}
