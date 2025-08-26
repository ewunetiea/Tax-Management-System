package com.afr.fms.Approver.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Approver.Mapper.AuditISMApproverMapper;
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
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class AuditISMApproverService {
    @Autowired
    private AuditISMApproverMapper auditMapper;

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

    private static final Logger logger = LoggerFactory.getLogger(AuditISMApproverService.class);

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

    public List<AuditISM> getAuditsForApprover(String category) {
        return auditMapper.getAuditsForApprover(category);
    }

    public void approveAudit(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getApprover().getId());
        auditMapper.approveAudit(audit);
        recentActivity = new RecentActivity();
        recentActivity.setMessage("Finding with case number  " + audit.getCase_number() + " is approved.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public List<AuditISM> getApprovedFindings(AuditISM auditISM) {
        PageHelper.startPage(auditISM.getPage_number(), auditISM.getPage_size());
        Page<AuditISM> auditsPage = (Page<AuditISM>) auditMapper.getApprovedFindings(auditISM,
                checkRequistionDates(auditISM.getFinding_dates(), 0) ? auditISM.getFinding_dates().get(0)
                        : null,
                checkRequistionDates(auditISM.getFinding_dates(), 1) ? auditISM.getFinding_dates().get(1)
                        : null);

        List<AuditISM> auditISMs = auditsPage.getResult(); // Get the actual list of audits from the page object
        if (auditISMs.size() > 0) {
            auditISMs.get(0).setTotal_records(auditsPage.getTotal());
        }
        return auditISMs;
    }

    public boolean checkRequistionDates(List<String> finding_dates, int index) {
        if (finding_dates != null) {
            if (index == 0)
                return finding_dates.size() > 0;
            return finding_dates.size() > 1;
        }
        return false;
    }

    public void cancelApprovalISM_Audit(AuditISM audit) {

        User user = userMapper.getAuditorById(audit.getApprover().getId());

        auditMapper.cancelApprovalISM_Audit(audit);

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Audit with case number  " + audit.getCase_number() + " is unapproved.");

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

    public void sendEmailtoAuditee(AuditISM audit, List<User> users) {
        List<User> uniqueUsers = filterUniqueUsers(users);
        for (User user : uniqueUsers) {
            audit.setReviewer(user);
            Approver_for_auditeeContext emailContext = new Approver_for_auditeeContext();
            emailContext.init(audit);
            emailContext.buildDisbursmentUrl(baseURL);
            try {
                emailService.sendMail(emailContext);
            } catch (MessagingException e) {
                logger.error("Error sending email to {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }

    public List<AuditISM> getRejectedFindings(Long approver_id) {
        return auditMapper.getRejectedFindings(approver_id);
    }

    public List<AuditISM> getApprovedFindingsStatus(Long approver_id) {
        // return auditMapper.getApprovedFindingsStatus(approver_id);
        List<AuditISM> auditISMs = auditMapper.getApprovedFindingsStatus(approver_id);
        return attachAuditeeResponse(auditISMs);
    }

    public void approveSelectedFindings(List<AuditISM> audits) {
        User approver = audits.get(0).getApprover();
        for (AuditISM auditISM : audits) {
            auditISM.setApprover(approver);
            auditMapper.approveAudit(auditISM);

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
                logger.error("Error while sending email to auditee: {}", e.getMessage());
            }
        }
    }

    public void approveSelectedRectifiedFindings(Remark remark) {
        remark.setRejected(true);
        List<AuditISM> audits = remark.getAudits();
        User approver = audits.get(0).getApprover();
        int status = audits.get(0).getRectification_approval_status().equals("approved") ? 1 : 2;
        String message = status == 1 ? "approved" : "rejected";
        String finding_status = audits.get(0).getFinding_status().equals("approved") ? "Rectification Approved"
                : "Rectification Approval Pending";
        for (AuditISM auditISM : audits) {
            auditISM.setApprover(approver);
            IS_MGT_Auditee is_MGT_Auditee = auditISM.getIS_MGTAuditee().get(0);
            is_MGT_Auditee.setApprover_id(approver.getId());
            is_MGT_Auditee.setApproval_status(status);
            is_MGT_Auditee.setFinding_status(finding_status);
            auditMapper.approveRectifiedFindings(is_MGT_Auditee);

            try {
                if (status == 2) {
                    remark.setAudit(auditISM);
                    remark.setReciever(auditISM.getFollowup_officer());
                    remarkService.addRemark(remark);
                }

                RecentActivity recentActivity = new RecentActivity();
                recentActivity.setMessage(
                        " Finding " + auditISM.getCase_number() + " rectification is " + message);
                recentActivity.setUser(auditISM.getApprover());
                recentActivityMapper.addRecentActivity(recentActivity);

            } catch (Exception e) {
                logger.error("Error while adding recent activity: {}", e.getMessage());
            }

        }

    }

    public void cancelSelectedFindings(List<AuditISM> audits) {
        User approver = audits.get(0).getApprover();
        for (AuditISM auditISM : audits) {
            auditISM.setApprover(approver);
            auditMapper.cancelApprovalISM_Audit(auditISM);

            AuditISM audit = auditISMMapper.getAudit(auditISM.getId());
            recentActivity = new RecentActivity();
            recentActivity.setMessage("Audit with case number  " + audit.getCase_number() + " is unapproved.");
            recentActivity.setUser(approver);
            recentActivityMapper.addRecentActivity(recentActivity);
            audit = new AuditISM();

        }
    }

    public void rejectFinding(Remark remark) {
        auditMapper.rejectFinding(remark.getAudit());
        remark.setRejected(true);
        remarkService.addRemark(remark);

        User user = userMapper.getAuditorById(remark.getAudit().getApprover().getId());
        recentActivity = new RecentActivity();
        recentActivity.setMessage("Audit with case number  " + remark.getAudit().getCase_number() + " is rejected.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

}
