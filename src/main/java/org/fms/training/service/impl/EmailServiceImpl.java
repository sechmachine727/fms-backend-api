package org.fms.training.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.fms.training.entity.EmailTemplate;
import org.fms.training.repository.EmailTemplateRepository;
import org.fms.training.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailTemplateRepository emailTemplateRepository;
    private final SpringTemplateEngine templateEngine;
    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) throws MessagingException {
        // Debug: Log template name and variables
        System.out.println("Template name: " + templateName);
        System.out.println("Variables: " + variables);

        // Fetch the template from the database using the template name
        EmailTemplate template = emailTemplateRepository.findByName(templateName)
                .orElseThrow(() -> new IllegalArgumentException("Email template not found with name: " + templateName));
        System.out.println("Template content length: " + template.getContent().length());

        // Debug: Log content
        System.out.println("Template content: " + template.getContent());
        StringSubstitutor sub = new StringSubstitutor(variables);
        // Use the fetched template content (HTML stored in DB)
        String templateContent = template.getContent();
        String htmlContent = sub.replace(templateContent);

        // Debug: Log final HTML content after substitution
        System.out.println("HTML Content after substitution: " + htmlContent);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("datvip1938@gmail.com");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // Enable HTML

        mailSender.send(message);
    }


}
