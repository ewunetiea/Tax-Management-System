package com.afr.fms.Security.email.context;

import org.springframework.web.util.UriComponentsBuilder;

import com.afr.fms.Auditor.Entity.AuditISM;

public class Approver_for_auditeeContext extends AbstractEmailContext {

    private AuditISM audit;

    @Override
    public <T> void init(T context) {
        // we can do any common configuration setup here
        // like setting up some base URL and context
        AuditISM audit = (AuditISM) context;

        // String directorate_name = audit.getReviewer().getBranch().getName();
        // String auditee_email = "abebayehual@awashbank.com";
        String auditee_email = audit.getReviewer().getEmail();

        String director_name = audit.getReviewer().getFirst_name() + " " + audit.getReviewer().getMiddle_name();

        String approver_name = audit.getApprover().getFirst_name() + " " + audit.getApprover().getMiddle_name();

        String title = "Response Request for Approved Audit Findings";

        String emailMessagePart1 = "I hope this email finds you well.";
        String emailMessagePart2 = "The audit findings have been approved by " + approver_name
                + ". Please take a moment to review the finalized findings report and provide your response or proposed action plan accordingly.";
        String emailMessagePart3 = "Your cooperation and timely response are essential to address the identified issues effectively and ensure compliance with our audit objectives.";
        String emailMessagePart4 = "Should you have any questions or require further clarification on the findings, please feel free to contact me.";
        String emailMessagePart5 = "Thank you for your attention to this matter.";
        String emailMessagePart6 = "Best regards,";

        put("title", title);
        put("director_name", "Dear " + director_name + ",");
        put("emailMessagePart1", emailMessagePart1);
        put("emailMessagePart2", emailMessagePart2);
        put("emailMessagePart3", emailMessagePart3);
        put("emailMessagePart4", emailMessagePart4);
        put("emailMessagePart5", emailMessagePart5);
        put("emailMessagePart6", emailMessagePart6);
        put("approver_name", approver_name);

        setTemplateLocation("ism_audit/approver_for_auditee");
        setSubject(title);
        setFrom("AFRFMS@awashbank.com");
        setTo(auditee_email);
    }

    public void setISMAudit(AuditISM audit) {
        this.audit = audit;
        // put("token", this.token);
    }

    public void buildDisbursmentUrl(final String baseURL) {
        // final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
        // .path("/manage-audit").toUriString();
        final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/").toUriString();
        put("redirectURL", url);
    }

}
