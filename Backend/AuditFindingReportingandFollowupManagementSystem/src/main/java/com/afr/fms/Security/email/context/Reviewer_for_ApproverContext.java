// package com.afr.fms.Security.email.context;

// import org.springframework.web.util.UriComponentsBuilder;
// import com.afr.fms.Auditor.Entity.AuditISM;

// public class Reviewer_for_ApproverContext extends AbstractEmailContext {

//     private AuditISM audit;

//     @Override
//     public <T> void init(T context) {
//         // we can do any common configuration setup here
//         // like setting up some base URL and context
//         AuditISM audit = (AuditISM) context; // we pass the customer information

       

//         String approver_email = audit.getUsers().get(0).getEmail();
//         // String approver_email = "abebayehual@awashbank.com";

//         String approver_name = audit.getUsers().get(0).getFirst_name() + " " + audit.getUsers().get(0).getMiddle_name();

//         String reviewer_name = audit.getReviewer().getFirst_name() + " " + audit.getReviewer().getMiddle_name();

//         // String title = "Finding with case number: " + audit.getCase_number() + " for " + auditees;
//          String title = "Approval Request for Audit Findings";


//         String emailMessagePart1 = "I hope this email finds you well." ;
//         String emailMessagePart2 = "As the reviewer of the audit findings, I am forwarding them to you for your approval. Your guidance and expertise are vital in ensuring the accuracy and appropriateness of the findings before they are finalized." ;
//         String emailMessagePart3 = "Your prompt approval would be greatly appreciated.";
//         String emailMessagePart4 = "Should you have any questions or require further clarification, please feel free to reach out to me.";
//         String emailMessagePart5 = "Thank you for your attention to this matter.";
//         String emailMessagePart6 = "Best regards,"; 

//         put("title", title);
//         put("approver_name","Dear "+ approver_name+",");
//         put("emailMessagePart1", emailMessagePart1);
//         put("emailMessagePart2", emailMessagePart2);
//         put("emailMessagePart3", emailMessagePart3);
//         put("emailMessagePart4", emailMessagePart4);
//         put("emailMessagePart5", emailMessagePart5);
//         put("emailMessagePart6", emailMessagePart6);
//         put("reviewer_name", reviewer_name);

//         setTemplateLocation("ism_audit/reviewer_for_approver");
//         setSubject(title);
//         setFrom("AFRFMS@awashbank.com");
//         setTo(approver_email);
//     }

//     public void setISMAudit(AuditISM audit) {
//         this.audit = audit;
//         // put("token", this.token);
//     }

//     public void buildApproveRequestUrl(final String baseURL) {
//         // final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
//         // .path("/manage-audit").toUriString();
//         final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
//                 .path("/").toUriString();
//         put("redirectURL", url);
//     }

// }
