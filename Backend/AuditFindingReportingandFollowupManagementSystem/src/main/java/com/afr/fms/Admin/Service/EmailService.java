// package com.afr.fms.Admin.Service;
// import javax.mail.MessagingException;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeMessage;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;
// @Service
// public class EmailService {

//     @Autowired
//     private JavaMailSender mailSender;

//     // public void sendEmail(String to, String subject, String body) {
//     //     SimpleMailMessage message = new SimpleMailMessage();
//     //     message.setTo(to);
//     //     message.setSubject(subject);
//     //     message.setText(body);
//     //     mailSender.send(message);
//     // }
//     public void sendHtmlEmail() throws MessagingException {
//         MimeMessage message = mailSender.createMimeMessage();
    
//         message.setFrom(new InternetAddress("abebayehualaro@gmail.com"));
//         message.setRecipients(MimeMessage.RecipientType.TO, "abebayehualaro1@gmail.com");
//         message.setSubject("Test email from Spring");
    
//         String htmlContent = "<h1>This is a test Spring Boot email</h1>" +
//                              "<p>It can contain <strong>HTML</strong> content.</p>";
//         message.setContent(htmlContent, "text/html; charset=utf-8");
    
//         mailSender.send(message);
//     }
// }