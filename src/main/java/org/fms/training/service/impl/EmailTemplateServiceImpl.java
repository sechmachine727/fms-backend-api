package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.entity.EmailTemplate;
import org.fms.training.repository.EmailTemplateRepository;
import org.fms.training.service.EmailTemplateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    public List<EmailTemplate> getAllEmailTemplates() {
        return emailTemplateRepository.findAll();
    }

    @Override
    public EmailTemplate getEmailTemplateById(Long id) {
        return emailTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Email template not found with id: " + id));
    }

    @Override
    public EmailTemplate updateEmailTemplate(Long id, String subject, String content) {
        EmailTemplate emailTemplate = getEmailTemplateById(id);
        emailTemplate.setSubject(subject);
        emailTemplate.setContent(content);
        return emailTemplateRepository.save(emailTemplate);
    }

    @Override
    public EmailTemplate getTemplateByName(String name) {
        return emailTemplateRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Email template not found with name: " + name));
    }


}
