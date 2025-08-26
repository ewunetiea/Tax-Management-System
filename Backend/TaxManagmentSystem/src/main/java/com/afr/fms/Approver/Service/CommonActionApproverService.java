package com.afr.fms.Approver.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Approver.Mapper.AuditISMApproverMapper;
import com.afr.fms.Approver.Mapper.CommonActionApproverMapper;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.Audit_Remark.RemarkService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Security.email.context.Approver_for_auditeeContext;
import com.afr.fms.Security.email.service.EmailService;

@Service
public class CommonActionApproverService {
    
    @Autowired
    private AuditISMApproverMapper auditMapper;

    @Autowired
    private CommonActionApproverMapper commonActionApproverMapper;

    @Autowired
    private AuditISMMapper auditISMMapper;

    private String baseURL = Endpoint.URL;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RemarkService remarkService;
    @Autowired
    private RecentActivityMapper recentActivityMapper;

    @Autowired
    private AuditeeDivisionISMMapper auditeeDivisionISMMapper;

    private RecentActivity recentActivity;

    public void approveSelectedFindings(List<AuditISM> audits) {
        User approver = audits.get(0).getApprover();
        for (AuditISM auditISM : audits) {
            auditISM.setApprover(approver);
            commonActionApproverMapper.approveAudit(auditISM);
            
            AuditISM audit = auditISMMapper.getAudit(auditISM.getId());
            recentActivity = new RecentActivity();
            recentActivity.setMessage("Finding with case number  " + audit.getCase_number() + " is approved.");
            recentActivity.setUser(approver);
            recentActivityMapper.addRecentActivity(recentActivity);
            audit = new AuditISM();
        }


        if (audits.get(0).getIs_email()) {
            AuditISM audit2 = auditISMMapper.getAudit(audits.get(0).getId());
            List<Long> branch_ids = new ArrayList<>();
            for (IS_MGT_Auditee is_MGT_Auditee : audit2.getIS_MGTAuditee()) {
                branch_ids.add(is_MGT_Auditee.getAuditee_id());
            }
            List<User> users = new ArrayList<>();
            for (Long branch_id : branch_ids) {
                users.addAll(userMapper.getUserByBranchandRole(branch_id, "AUDITEE"));
            }
            try {
                sendEmailtoAuditee(audit2, users);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void cancelSelectedFindings(List<AuditISM> audits) {
        User approver = audits.get(0).getApprover();
        for (AuditISM auditISM : audits) {
            auditISM.setApprover(approver);
            commonActionApproverMapper.cancelApprovalISM_Audit(auditISM);

            AuditISM audit = auditISMMapper.getAudit(auditISM.getId());
            recentActivity = new RecentActivity();
            recentActivity.setMessage("Audit with case number  " + audit.getCase_number() + " is unapproved.");
            recentActivity.setUser(approver);
            recentActivityMapper.addRecentActivity(recentActivity);
            audit = new AuditISM();

        }
    }

    public void rejectFinding(Remark remark) {
        commonActionApproverMapper.rejectFinding(remark.getAudit());
        remark.setRejected(true);
        remarkService.addRemark(remark);

        User user = userMapper.getAuditorById(remark.getAudit().getApprover().getId());
        recentActivity = new RecentActivity();
        recentActivity.setMessage("Audit with case number  " + remark.getAudit().getCase_number() + " is rejected.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void cancelApprovalISM_Audit(AuditISM audit) {

        User user = userMapper.getAuditorById(audit.getApprover().getId());

        commonActionApproverMapper.cancelApprovalISM_Audit(audit);

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Audit with case number  " + audit.getCase_number() + " is unapproved.");

        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

   
    public void sendEmailtoAuditee(AuditISM audit, List<User> users) {
        List<User> uniqueUsers = filterUniqueUsers(users);
        for (User user : uniqueUsers) {
            audit.setReviewer(user);
            Approver_for_auditeeContext emailContext = new Approver_for_auditeeContext();
            emailContext.init(audit);
            emailContext.buildDisbursmentUrl(baseURL);
            try {
                emailService.sendMail(emailContext);
                System.out.println("Email to auditee is sent");
            } catch (MessagingException e) {
                System.out.println(e);
            }
        }
    }

    public void approveAudit(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getApprover().getId());
        commonActionApproverMapper.approveAudit(audit);
        recentActivity = new RecentActivity();
        recentActivity.setMessage("Finding with case number  " + audit.getCase_number() + " is approved.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    
    public List<User> filterUniqueUsers(List<User> users) {
        List<User> uniqueUsers = users.stream()
                .collect(Collectors.toMap(User::getId, u -> u, (oldUser, newUser) -> oldUser))
                .values().stream()
                .collect(Collectors.toList());
        return uniqueUsers;
    }

}
