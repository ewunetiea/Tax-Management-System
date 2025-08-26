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
import com.afr.fms.Auditor.Mapper.UploadFileISMMapper;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.ChangeTrackerISMService;
import com.afr.fms.Common.Audit_Change_Tracker.ISM.Change_Tracker_ISM;
import com.afr.fms.Common.Entity.PaginatorPayLoad;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.email.context.Auditor_for_ReviewerContext;
import com.afr.fms.Security.email.service.EmailService;

@Service
public class commonMGTService {

    private String baseURL = Endpoint.URL;

    @Autowired
    private AuditISMMapper auditMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChangeTrackerISMService changeTrackerISMService;

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

    public List<AuditISM> getPassedAuditsForAuditor(PaginatorPayLoad paginatorPayLoad) {
        return auditMapper.getPassedAuditsForAuditor(paginatorPayLoad);
    }

    public List<AuditISM> getRejectedAuditsForAuditor(Long auditor_id) {
        return auditMapper.getRejectedAuditsForAuditor(auditor_id);
    }

    public List<AuditISM> getAuditsOnDrafting(PaginatorPayLoad paginatorPayLoad) {
        return auditMapper.getAuditsOnDraftingMGT(paginatorPayLoad);
    }

    public List<AuditISM> getAuditsOnProgressForAuditor(Long auditor_id) {
        List<AuditISM> auditISMs = auditMapper.getAuditsOnProgressForAuditor(auditor_id);
        return attachAuditeeResponse(auditISMs);
    }

    public void createISMFinding(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getAuditor().getId());
        audit.setCategory(user.getCategory());
        audit.setCase_number(generateCaseNumber(audit.getCategory()));
        Long audit_id = (long) 0;
        if (audit.getCategory() == "IS") {
            audit_id = auditMapper.createISFinding(audit);
        } else {
            audit_id = auditMapper.createMGTFindingEnhanced(audit);
        }

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

    public void updateISMFinding(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getAuditor().getId());
        System.out.println(audit.getAuditees());
        if (audit.getCategory() == "IS") {
            commonActionAuditorMapper.updateISFinding(audit);
        } else if (audit.getFinding_detail() == null) {
            auditMapper.updateMGTFinding(audit);
        } else {
            auditMapper.updateMGTFindingandFindingDetail(audit);
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

    public void deleteISMFinding(Long id) {
        commonActionAuditorMapper.deleteISMFinding(id);
        AuditISM audit = auditMapper.getAudit(id);
        User user = new User();
        recentActivity = new RecentActivity();
        recentActivity.setMessage(" Finding with case number  " + audit.getCase_number() + " is deleted.");
        user.setId(audit.getAuditor().getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void deleteSelectedISMFinding(List<AuditISM> auditISMs) {
        for (AuditISM auditISM : auditISMs) {
            commonActionAuditorMapper.deleteISMFinding(auditISM.getId());
            AuditISM audit = auditMapper.getAudit(auditISM.getId());
            User user = new User();
            recentActivity = new RecentActivity();
            recentActivity.setMessage(" Audit with case number  " + audit.getCase_number() + " is deleted.");
            user.setId(audit.getAuditor().getId());
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);
            audit = new AuditISM();

        }
    }

    public void passISMFinding(Long id) {
        commonActionAuditorMapper.passISMFinding(id);
        AuditISM audit = auditMapper.getAudit(id);
        User user2 = new User();
        recentActivity = new RecentActivity();
        recentActivity
                .setMessage(" Finding with case number  " + audit.getCase_number() + " is passed to the reviewer.");
        user2.setId(audit.getAuditor().getId());
        recentActivity.setUser(user2);
        recentActivityMapper.addRecentActivity(recentActivity);

        // List<User> user = new ArrayList<>();

        // if (audit.getCategory().trim().equalsIgnoreCase("IS")) {
        // user = userMapper.getUserByCategoryandRole(audit.getCategory(),
        // "REVIEWER_IS");
        // } else {
        // user = userMapper.getUserByCategoryandRole(audit.getCategory(),
        // "REVIEWER_MGT");
        // }

        // audit.setUsers(user);
        // try {
        // sendEmailtoReviewer(audit);
        // } catch (Exception e) {
        // System.out.println(e);
        // }

    }

    public void passSelectedISMFinding(List<AuditISM> auditISMs) {
        for (AuditISM auditISM : auditISMs) {
            commonActionAuditorMapper.passISMFinding(auditISM.getId());
            AuditISM audit = auditMapper.getAudit(auditISM.getId());
            User user2 = new User();
            recentActivity = new RecentActivity();
            recentActivity
                    .setMessage(" Audit with case number  " + audit.getCase_number() + " is passed to the reviewer.");

            user2.setId(audit.getAuditor().getId());
            recentActivity.setUser(user2);
            recentActivityMapper.addRecentActivity(recentActivity);
            audit = new AuditISM();

        }

        if (auditISMs.get(0).getIs_email()) {
            AuditISM audit = auditMapper.getAudit(auditISMs.get(0).getId());
            List<User> user = new ArrayList<>();

            if (audit.getCategory().trim().equalsIgnoreCase("IS")) {
                user = userMapper.getUserByCategoryandRole(audit.getCategory(),
                        "REVIEWER_IS");
            } else {
                user = userMapper.getUserByBankingandRole(audit.getManagement(),
                        "REVIEWER_MGT");
            }
            audit.setUsers(user);
            try {
                sendEmailtoReviewer(audit);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    public void backISMFinding(Long id) {

        commonActionAuditorMapper.backISMFinding(id);
        AuditISM audit = auditMapper.getAudit(id);
        User user = new User();
        recentActivity = new RecentActivity();
        recentActivity
                .setMessage(
                        " Finding with case number  " + audit.getCase_number() + " is moved to the drafting state.");

        user.setId(audit.getAuditor().getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void backSelectedISMFinding(List<AuditISM> auditISMs) {
        for (AuditISM auditISM : auditISMs) {

            commonActionAuditorMapper.backISMFinding(auditISM.getId());

            AuditISM audit = auditMapper.getAudit(auditISM.getId());
            User user = new User();
            recentActivity = new RecentActivity();
            recentActivity
                    .setMessage(" Finding with case number  " + audit.getCase_number()
                            + " is moved to the drafting state.");

            user.setId(audit.getAuditor().getId());
            recentActivity.setUser(user);
            recentActivityMapper.addRecentActivity(recentActivity);
            audit = new AuditISM();

        }
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
