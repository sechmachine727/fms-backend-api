package org.fms.training.service;

import org.fms.training.common.entity.EmailTemplate;

import java.util.List;

public interface EmailTemplateService {
    List<EmailTemplate> getAllEmailTemplates();

    EmailTemplate getEmailTemplateById(Long id);

    EmailTemplate updateEmailTemplate(Long id, String subject, String content);

    EmailTemplate getTemplateByName(String name);
}
