// package com.afr.fms.Security.email.context;

// import org.springframework.web.util.UriComponentsBuilder;

// import com.afr.fms.Auditor.Entity.AuditISM;

// public class Auditee_for_FollowupOfficerContext extends AbstractEmailContext {

//         // private AuditISM audit;

//         @Override
//         public <T> void init(T context) {
//                 // we can do any common configuration setup here
//                 // like setting up some base URL and context
//                 AuditISM auditISM = (AuditISM) context; // we pass the customer information

//                 // String followupofficer_email = "abebayehual@awashbank.com";
//                 String followupofficer_email = auditISM.getUsers().get(0).getEmail();
//                 String followupofficer_name = auditISM.getUsers().get(0).getFirst_name() + " "
//                                 + auditISM.getUsers().get(0).getMiddle_name();

//                 String auditee_name = auditISM.getUsers().get(1).getFirst_name() + " "
//                                 + auditISM.getUsers().get(1).getMiddle_name();

//                 String title = "Response to Audit Findings";

//                 String emailMessagePart1 = "I hope this email finds you well.";
//                 String emailMessagePart2 = "I wanted to inform you that I have provided my response to the audit findings through our Audit Management System. You can access the detailed response document and any associated information directly in the system.";
//                 String emailMessagePart3 = "Your feedback and guidance on the response would be greatly appreciated to ensure the accuracy and completeness of our actions to address the identified issues.";
//                 String emailMessagePart4 = "Should you have any questions or require assistance in accessing the response in the system, please let me know, and I will be happy to assist.";
//                 String emailMessagePart5 = "Thank you for your attention to this matter.";
//                 String emailMessagePart6 = "Best regards,";

//                 put("title", title);
//                 put("followupofficer_name", "Dear " + followupofficer_name + ",");
//                 put("emailMessagePart1", emailMessagePart1);
//                 put("emailMessagePart2", emailMessagePart2);
//                 put("emailMessagePart3", emailMessagePart3);
//                 put("emailMessagePart4", emailMessagePart4);
//                 put("emailMessagePart5", emailMessagePart5);
//                 put("emailMessagePart6", emailMessagePart6);
//                 put("auditee_name", auditee_name);

//                 // put("firstName", user.getFirst_name());
//                 // put("password", user.getPassword());

//                 setTemplateLocation("ism_audit/auditee_for_followupofficer");
//                 setSubject(title);
//                 setFrom("AFRFMS@awashbank.com");
//                 setTo(followupofficer_email);
//         }

//         public void buildFollowupOfficerUrl(final String baseURL) {
//                 // final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
//                 // .path("/manage-audit").toUriString();
//                 final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
//                                 .path("/").toUriString();
//                 put("redirectURL", url);
//         }

// }
