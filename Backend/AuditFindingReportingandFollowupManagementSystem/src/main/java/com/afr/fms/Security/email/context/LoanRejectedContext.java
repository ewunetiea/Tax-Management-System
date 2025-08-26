// package com.afr.fms.Security.email.context;

// import java.text.DecimalFormat;
// import java.text.DecimalFormatSymbols;
// import java.text.NumberFormat;

// import org.springframework.web.util.UriComponentsBuilder;

// import com.afr.fms.Maker.Entity.Loan;

// public class LoanRejectedContext extends AbstractEmailContext {
//     private Loan loan;

//     @Override
//     public <T> void init(T context){
//         //we can do any common configuration setup here
//         // like setting up some base URL and context
//         Loan loan = (Loan) context; // we pass the customer information
//         NumberFormat formatter = NumberFormat.getCurrencyInstance();
//         DecimalFormatSymbols dfs = new DecimalFormatSymbols();
//         dfs.setCurrencySymbol("ETB");
//         dfs.setGroupingSeparator('.');
//         dfs.setMonetaryDecimalSeparator('.');
//         ((DecimalFormat) formatter).setDecimalFormatSymbols(dfs);

//         String title = loan.getLoanPurpose().getName() + " for " + loan.getStaff().getFirstName() + " " + loan.getStaff().getMiddleName()
//             + " " + loan.getStaff().getLastName();
        
//         String emailMessagePart1 = "Your loan request was recieved on " + loan.getRequest_date() 
//         + ", and after careful consideration, "+ "we have rejected loan request "
//         + "of "+ formatter.format(loan.getAmount())+" made for NGO Institution "
//         + loan.getInstitution().getName() +" staff member " + loan.getStaff().getFirstName() + " " + loan.getStaff().getMiddleName()
//             + " " + loan.getStaff().getLastName() + ".";
//         String emailMessagePart2 =  "If there are any inconvenience, please do not hesitate to contact us.";
//         String emailMessagePart3 = "Thank you for your understanding.";

//         String emailMessagePart4 = "Sincerly, " + loan.getApprover().getFirst_name() + " " + loan.getApprover().getLast_name();

//         put("title", title);
//         put("loanMakerName", loan.getUser().getFirst_name() + " " + loan.getUser().getLast_name());
//         put("emailMessagePart1", emailMessagePart1);
//         put("emailMessagePart2", emailMessagePart2);
//         put("emailMessagePart3", emailMessagePart3);
//         put("emailMessagePart4", emailMessagePart4);

//         // put("firstName", user.getFirst_name());
//         // put("password", user.getPassword());

//         setTemplateLocation("loans/rejected-loan");
//         setSubject("Loan requested status for NGO Staff " + loan.getStaff().getFirstName() + " " + loan.getStaff().getMiddleName()
//             + " " + loan.getStaff().getLastName());
//         setFrom("bigfcyAdmin@awashbank.com");
//         setTo(loan.getUser().getEmail());
//     }

//     public void setLoan(Loan loan) {
//         this.loan = loan;
//         // put("token", this.token);
//     }

//     // public void buildDisbursmentUrl(final String baseURL){
//     //     final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
//     //             .path("/manage-loan").toUriString();
//     //     put("redirectURL", url);
//     // }

    
// }
