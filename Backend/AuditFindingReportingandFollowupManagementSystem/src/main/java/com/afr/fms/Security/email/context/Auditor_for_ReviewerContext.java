// package com.afr.fms.Security.email.context;

// import org.springframework.web.util.UriComponentsBuilder;

// import com.afr.fms.Auditor.Entity.AuditISM;

// public class Auditor_for_ReviewerContext extends AbstractEmailContext {

//     private AuditISM audit;

//     @Override
//     public <T> void init(T context) {
//         // we can do any common configuration setup here
//         // like setting up some base URL and context
//         AuditISM audit = (AuditISM) context; // we pass the audit information

//         String auditor_name = audit.getAuditor().getFirst_name() + " " + audit.getAuditor().getMiddle_name();
//         String reviewer_email = audit.getUsers().get(0).getEmail();
//         // String reviewer_email = "abebayehual@awashbank.com";

//         String reviewer_name = audit.getUsers().get(0).getFirst_name() + " " + audit.getUsers().get(0).getMiddle_name();

//         String title = "Review Request: Identified Finding";

//         String emailMessagePart1 = "I hope this message finds you well.";
//         String emailMessagePart2 = "I wanted to bring to your attention a finding that I have identified during my audit process. After thorough consideration, I believe it would be beneficial for you to review it.";
//         String emailMessagePart3 = "Your expertise and insight would be invaluable in assessing the significance and potential implications of this finding. Please let me know when you have a moment to discuss it further or if you require any additional information from my end.";
//         String emailMessagePart4 = "Thank you for your attention to this matter.";
//         String emailMessagePart5 = "Best Regards,";

//         put("title", title);
//         put("reviewer_name", "Dear " + reviewer_name + ",");
//         put("emailMessagePart1", emailMessagePart1);
//         put("emailMessagePart2", emailMessagePart2);
//         put("emailMessagePart3", emailMessagePart3);
//         put("emailMessagePart4", emailMessagePart4);
//         put("emailMessagePart5", emailMessagePart5);
//         put("auditor_name", auditor_name);

//         // put("firstName", user.getFirst_name());
//         // put("password", user.getPassword());

//         setTemplateLocation("ism_audit/auditor_for_reviewer");
//         setSubject("Review Request: Identified Finding");
//         setFrom("AFRFMS@awashbank.com");
//         setTo(reviewer_email);
//     }

//     public void setISMAudit(AuditISM audit) {
//         this.audit = audit;
//         // put("token", this.token);
//     }

//     public void buildReviewRequestUrl(final String baseURL) {
//         // final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
//         // .path("/manage-audit").toUriString();
//         final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
//                 .path("/").toUriString();
//         put("redirectURL", url);
//     }

// }
