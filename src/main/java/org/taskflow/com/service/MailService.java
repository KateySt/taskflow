package org.taskflow.com.service;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface MailService {

    void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) throws MessagingException;

}
