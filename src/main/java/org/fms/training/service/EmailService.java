package org.fms.training.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
