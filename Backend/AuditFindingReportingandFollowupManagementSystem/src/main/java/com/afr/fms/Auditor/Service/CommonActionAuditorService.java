package com.afr.fms.Auditor.Service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Auditor.Mapper.CommonActionAuditorMapper;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.email.context.Auditor_for_ReviewerContext;
import com.afr.fms.Security.email.service.EmailService;

@Service
public class CommonActionAuditorService {
    private String baseURL = Endpoint.URL;

    @Autowired
    private CommonActionAuditorMapper commonActionAuditorMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    private RecentActivity recentActivity;

    public void deleteISMFinding(Long id) {

        commonActionAuditorMapper.deleteISMFinding(id);
        AuditISM audit = commonActionAuditorMapper.getAudit(id);
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
            AuditISM audit = commonActionAuditorMapper.getAudit(auditISM.getId());
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
        AuditISM audit = commonActionAuditorMapper.getAudit(id);
        User user2 = new User();
        recentActivity = new RecentActivity();
        recentActivity
                .setMessage(" Finding with case number  " + audit.getCase_number() + " is passed to the reviewer.");
        user2.setId(audit.getAuditor().getId());
        recentActivity.setUser(user2);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public void passSelectedISMFinding(List<AuditISM> auditISMs) {
        for (AuditISM auditISM : auditISMs) {
            commonActionAuditorMapper.passISMFinding(auditISM.getId());
            AuditISM audit = commonActionAuditorMapper.getAudit(auditISM.getId());

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
            AuditISM audit = commonActionAuditorMapper.getAudit(auditISMs.get(0).getId());
            List<User> user = new ArrayList<>();

            if (audit.getCategory().trim().equalsIgnoreCase("IS")) {
                user = userMapper.getUserByCategoryandRole(audit.getCategory(), "REVIEWER_IS");
            } else {
                user = userMapper.getUserByBankingandRole(audit.getManagement(), "REVIEWER_MGT");
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
        AuditISM audit = commonActionAuditorMapper.getAudit(id);
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

            AuditISM audit = commonActionAuditorMapper.getAudit(auditISM.getId());
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

    public void changeRecitificationStatus(AuditISM auditISM) {
        commonActionAuditorMapper.changeRecitificationStatus(auditISM);
    }

}
