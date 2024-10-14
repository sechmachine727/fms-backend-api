package org.fms.training.service;

import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, String text);

    void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables);
}
