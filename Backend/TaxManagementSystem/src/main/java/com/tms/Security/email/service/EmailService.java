package com.tms.Security.email.service;

import jakarta.mail.MessagingException;

import com.tms.Security.email.context.AbstractEmailContext;

public interface EmailService {

    void sendMail(final AbstractEmailContext email) throws MessagingException;
}
