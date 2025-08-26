package com.afr.fms.Reviewer.Service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.Role;
import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Auditee.Mapper.AuditeeDivisionISMMapper;
import com.afr.fms.Auditor.Entity.AuditISM;
import com.afr.fms.Auditor.Entity.IS_MGT_Auditee;
import com.afr.fms.Auditor.Mapper.AuditISMMapper;
import com.afr.fms.Common.Audit_Remark.Remark;
import com.afr.fms.Common.Audit_Remark.RemarkService;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Payload.endpoint.Endpoint;
import com.afr.fms.Reviewer.Mapper.ReviewerISMMapper;
import com.afr.fms.Security.email.context.Reviewer_for_ApproverContext;
import com.afr.fms.Security.email.service.EmailService;

@Service
public class ReviewerISMService {
    @Autowired
    private ReviewerISMMapper reviewerISMMapper;

    @Autowired
    private AuditISMMapper auditISMMapper;

    @Autowired
    private RemarkService remarkService;

    @Autowired
    private RecentActivityMapper recentActivityMapper;
    private RecentActivity recentActivity;

    private String baseURL = Endpoint.URL;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuditeeDivisionISMMapper auditeeDivisionISMMapper;

    public void reviewFindings(AuditISM audit) {

        reviewerISMMapper.reviewFindings(audit);
        AuditISM auditInfo = auditISMMapper.getAudit(audit.getId());

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Finding with case number  " + audit.getCase_number() + " is reviewed.");
        recentActivity.setUser(audit.getReviewer());
        recentActivityMapper.addRecentActivity(recentActivity);

        // List<User> user = new ArrayList<>();

        // if (auditInfo.getCategory().trim().equalsIgnoreCase("IS")) {
        // user = userMapper.getUserByCategoryandRole(auditInfo.getCategory(),
        // "APPROVER_IS");
        // } else {
        // user = userMapper.getUserByCategoryandRole(auditInfo.getCategory(),
        // "APPROVER_MGT");
        // }
        // auditInfo.setUsers(user);
        // try {
        // sendEmailtoApprover(auditInfo);
        // } catch (Exception e) {
        // System.out.print(e);
        // }

    }

    public void cancelFinding(Remark remark) {

        User user = userMapper.getAuditorById(remark.getAudit().getReviewer().getId());

        reviewerISMMapper.cancelFinding(remark.getAudit());
        remark.setRejected(true);
        remarkService.addRemark(remark);
        recentActivity = new RecentActivity();
        recentActivity.setMessage("Finding with case number  " + remark.getAudit().getCase_number() + " is rejected.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public void unReviewISM_Audit_Findings(AuditISM audit) {
        User user = userMapper.getAuditorById(audit.getReviewer().getId());
        reviewerISMMapper.unReviewISM_Audit_Findings(audit);
        recentActivity = new RecentActivity();
        recentActivity.setMessage(" Finding with case number  " + audit.getCase_number() + " is unreviewed.");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public List<AuditISM> getReviewedFindings(Long reviewer_d) {
        return reviewerISMMapper.getReviewedFindings(reviewer_d);
    }

    public List<AuditISM> getApproverRejectedFindings(Long reviewer_d) {
        return reviewerISMMapper.getApproverRejectedFindings(reviewer_d);
    }

    public List<AuditISM> getRejectedFindings(Long reviewer_d) {
        return reviewerISMMapper.getRejectedFindings(reviewer_d);
    }

    public List<AuditISM> getReviewedFindingsStatus(Long reviewer_d) {
        return reviewerISMMapper.getReviewedFindingsStatus(reviewer_d);
    }

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

    public List<AuditISM> getAuditsForReviewer(User user) {
        if (user.getCategory().equalsIgnoreCase("is")) {
            return reviewerISMMapper.getAuditsForReviewerIS(user.getCategory());
        } else {
            return reviewerISMMapper.getAuditsForReviewerMGT(user);
        }
    }

    public void multiReviewFindings(List<AuditISM> audits) {
        User reviewer = audits.get(0).getReviewer();

        for (AuditISM auditISM : audits) {
            auditISM.setReviewer(reviewer);
            reviewerISMMapper.reviewFindings(auditISM);

            recentActivity = new RecentActivity();
            recentActivity.setMessage("Finding with case number  " + auditISM.getCase_number() + " is reviewed.");
            recentActivity.setUser(reviewer);
            recentActivityMapper.addRecentActivity(recentActivity);
        }

        if (audits.get(0).getIs_email()) {
            AuditISM auditInfo = auditISMMapper.getAudit(audits.get(0).getId());
            List<User> user = new ArrayList<>();
            if (auditInfo.getCategory().trim().equalsIgnoreCase("IS")) {
                user = userMapper.getUserByCategoryandRole(auditInfo.getCategory(),
                        "APPROVER_IS");
            } else {
                user = userMapper.getUserByCategoryandRole(auditInfo.getCategory(),
                        "APPROVER_MGT");
            }
            auditInfo.setUsers(user);
            try {
                sendEmailtoApprover(auditInfo);
            } catch (Exception e) {
                System.out.print(e);
            }
        }

    }

    public void unReview_Multi_Findings(List<AuditISM> audits) {
        User reviewer = audits.get(0).getReviewer();
        for (AuditISM audit : audits) {

            reviewerISMMapper.unReviewISM_Audit_Findings(audit);

            AuditISM a = auditISMMapper.getAudit(audit.getId());
            recentActivity = new RecentActivity();
            recentActivity.setMessage("Finding with case number  " + audit.getCase_number() + " is unreviewed.");
            recentActivity.setUser(reviewer);
            recentActivityMapper.addRecentActivity(recentActivity);
            a = new AuditISM();

        }
    }

    public void sendEmailtoApprover(AuditISM audit) {
        Reviewer_for_ApproverContext emailContext = new Reviewer_for_ApproverContext();
        emailContext.init(audit);
        emailContext.buildApproveRequestUrl(baseURL);
        try {
            emailService.sendMail(emailContext);
            System.out.println("Audit Approve Request is sent");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

}
