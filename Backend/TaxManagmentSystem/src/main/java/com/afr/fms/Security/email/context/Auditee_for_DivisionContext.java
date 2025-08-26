package com.afr.fms.Security.email.context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.util.UriComponentsBuilder;
import com.afr.fms.Auditor.Entity.AuditISM;

public class Auditee_for_DivisionContext extends AbstractEmailContext {

        // private AuditISM audit;

        @Override
        public <T> void init(T context) {
                // we can do any common configuration setup here
                // like setting up some base URL and context
                AuditISM auditISM = (AuditISM) context; // we pass the customer information

                // String division_email = "abebayehual@awashbank.com";
                String auditee_name = auditISM.getAuditee().getFirst_name() + " "
                                + auditISM.getAuditee().getMiddle_name();
                String division_email = auditISM.getEditor().getEmail();
                String division_name = auditISM.getEditor().getFirst_name() + " "
                                + auditISM.getEditor().getMiddle_name();

                // Input format: "2025-05-21 12:07:35.367"
                DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

                // Desired output format: "May 21, 2025"
                DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

                // Parse and format
                LocalDateTime dateTime = LocalDateTime.parse(auditISM.getFinding_date(), inputFormat);
                String formattedDate = dateTime.toLocalDate().format(outputFormat);
                
                String title = "Response Request for Assigned Audit Finding";

                String emailMessagePart1 = "I hope this message finds you well.";
                String emailMessagePart2 = "The audit finding with case number " + auditISM.getCase_number() +
                                ", reported on " + formattedDate
                                + ", has been assigned to you. I kindly request you to review the finding and provide your response or proposed action plan.";
                String emailMessagePart3 = "Your timely response is important to address the identified issue and ensure compliance with our audit standards.";
                String emailMessagePart4 = "If you have any questions or need further clarification, please feel free to contact me.";
                String emailMessagePart5 = "Thank you for your cooperation.";
                String emailMessagePart6 = "Best regards,";

                String subject = "Action Required: Response Needed for Assigned Audit Finding (Case No. "
                                + auditISM.getCase_number() + ")";

                put("title", title);
                put("division", "Dear " + division_name + ",");
                put("emailMessagePart1", emailMessagePart1);
                put("emailMessagePart2", emailMessagePart2);
                put("emailMessagePart3", emailMessagePart3);
                put("emailMessagePart4", emailMessagePart4);
                put("emailMessagePart5", emailMessagePart5);
                put("emailMessagePart6", emailMessagePart6);
                put("auditee_name", auditee_name);

                setTemplateLocation("ism_audit/auditee_for_division");
                setSubject(subject);
                setFrom("AFRFMS@awashbank.com");
                setTo(division_email);
        }

        public void buildDivisionUrl(final String baseURL) {
                // final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
                // .path("/manage-audit").toUriString();
                final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
                                .path("/").toUriString();
                put("redirectURL", url);
        }

}
