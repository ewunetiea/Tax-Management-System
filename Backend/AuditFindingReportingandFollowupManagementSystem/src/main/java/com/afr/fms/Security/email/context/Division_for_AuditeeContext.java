package com.afr.fms.Security.email.context;

import org.springframework.web.util.UriComponentsBuilder;
import com.afr.fms.Auditor.Entity.AuditISM;

public class Division_for_AuditeeContext extends AbstractEmailContext {

    private AuditISM audit;

    @Override
    public <T> void init(T context) {
        AuditISM audit = (AuditISM) context;

        String auditee_name = audit.getAuditee().getBranch().getName();
        String auditee_email = audit.getAuditee().getEmail();
        String division_name = audit.getDivision_name();

        // New: concatenate list of case numbers
        String joinedCaseNumbers = audit.getCase_number();

        String title = "Email Notification: Audit Findings Response Submission to Directorate";
        String emailMessagePart1 = "I hope this email finds you well.";
        String emailMessagePart2 = "The audit findings for case number(s) [" + joinedCaseNumbers
                + "] have been responded by "
                + division_name
                + ". Please take a moment to review the finalized findings report and take action accordingly.";
        String emailMessagePart3 = "Your cooperation and timely response are essential to address the identified issues effectively and ensure compliance with our audit objectives.";
        String emailMessagePart4 = "Should you have any questions or require further clarification on the findings, please feel free to contact me.";
        String emailMessagePart5 = "Thank you for your attention to this matter.";
        String emailMessagePart6 = "Best regards,";

        put("title", title);
        put("auditee_name", "Dear " + auditee_name + ",");
        put("emailMessagePart1", emailMessagePart1);
        put("emailMessagePart2", emailMessagePart2);
        put("emailMessagePart3", emailMessagePart3);
        put("emailMessagePart4", emailMessagePart4);
        put("emailMessagePart5", emailMessagePart5);
        put("emailMessagePart6", emailMessagePart6);
        put("division_name", division_name);

        setTemplateLocation("ism_audit/division_for_auditee");
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