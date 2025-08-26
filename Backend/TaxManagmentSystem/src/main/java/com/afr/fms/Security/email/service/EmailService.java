package com.afr.fms.Security.email.service;

import javax.mail.MessagingException;

import com.afr.fms.Security.email.context.AbstractEmailContext;

public interface EmailService {

    void sendMail(final AbstractEmailContext email) throws MessagingException;
}
