package org.fms.training.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.fms.training.common.entity.EmailTemplate;
import org.fms.training.repository.EmailTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailTemplateRepository emailTemplateRepository;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEmail_shouldSendSimpleMail() {
        // given
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Email Body";

        // when
        emailService.sendEmail(to, subject, text);

        // then
        then(mailSender).should(times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendHtmlEmail_shouldSendHtmlEmailWithTemplate() throws MessagingException {
        // given
        String to = "test@example.com";
        String subject = "HTML Subject";
        String templateName = "welcome";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "John Doe");

        EmailTemplate template = new EmailTemplate();
        template.setName(templateName);
        template.setContent("<html><body>Hello, ${name}!</body></html>");
        given(emailTemplateRepository.findByName(templateName)).willReturn(Optional.of(template));

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        emailService.sendHtmlEmail(to, subject, templateName, variables);

        // then
        then(mailSender).should(times(1)).send(mimeMessage);
    }

    @Test
    void sendHtmlEmail_shouldHandleTemplateNotFound() {
        // given
        String to = "test@example.com";
        String subject = "HTML Subject";
        String templateName = "nonexistent";
        Map<String, Object> variables = new HashMap<>();

        given(emailTemplateRepository.findByName(templateName)).willReturn(Optional.empty());

        // when
        emailService.sendHtmlEmail(to, subject, templateName, variables);

        // then
        then(mailSender).should(never()).send(any(MimeMessage.class));
        // Optionally, you can verify logging behavior here if logging is implemented
    }

    @Test
    void sendHtmlEmail_shouldHandleMessagingException() throws Exception {
        // given
        String to = "test@example.com";
        String subject = "HTML Subject";
        String templateName = "welcome";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "John Doe");

        EmailTemplate template = new EmailTemplate();
        template.setName(templateName);
        template.setContent("<html><body>Hello, ${name}!</body></html>");
        given(emailTemplateRepository.findByName(templateName)).willReturn(Optional.of(template));

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Simulate an exception during email sending
        doThrow(new RuntimeException("Test exception")).when(mailSender).send(mimeMessage);

        // when
        emailService.sendHtmlEmail(to, subject, templateName, variables);

        // then
        then(mailSender).should(times(1)).send(mimeMessage);
        // Optional: If you use a logger, you can verify if the exception was logged
    }

}
