package org.fms.training.service.impl;

import org.fms.training.common.entity.EmailTemplate;
import org.fms.training.repository.EmailTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmailTemplateServiceImplTest {

    @Mock
    private EmailTemplateRepository emailTemplateRepository;

    @InjectMocks
    private EmailTemplateServiceImpl emailTemplateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmailTemplates_shouldReturnListOfEmailTemplates() {
        // given
        EmailTemplate template1 = new EmailTemplate();
        EmailTemplate template2 = new EmailTemplate();
        given(emailTemplateRepository.findAll()).willReturn(List.of(template1, template2));

        // when
        List<EmailTemplate> templates = emailTemplateService.getAllEmailTemplates();

        // then
        assertThat(templates).hasSize(2);
        verify(emailTemplateRepository, times(1)).findAll();
    }

    @Test
    void getEmailTemplateById_shouldReturnTemplate() {
        // given
        Long id = 1L;
        EmailTemplate template = new EmailTemplate();
        template.setId(id);
        given(emailTemplateRepository.findById(id)).willReturn(Optional.of(template));

        // when
        EmailTemplate result = emailTemplateService.getEmailTemplateById(id);

        // then
        assertThat(result).isEqualTo(template);
        verify(emailTemplateRepository, times(1)).findById(id);
    }

    @Test
    void getEmailTemplateById_shouldThrowExceptionWhenNotFound() {
        // given
        Long id = 1L;
        given(emailTemplateRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> emailTemplateService.getEmailTemplateById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email template not found with id: " + id);
        verify(emailTemplateRepository, times(1)).findById(id);
    }

    @Test
    void updateEmailTemplate_shouldUpdateAndReturnTemplate() {
        // given
        Long id = 1L;
        String newSubject = "Updated Subject";
        String newContent = "Updated Content";
        EmailTemplate template = new EmailTemplate();
        template.setId(id);
        template.setSubject("Old Subject");
        template.setContent("Old Content");
        given(emailTemplateRepository.findById(id)).willReturn(Optional.of(template));
        given(emailTemplateRepository.save(template)).willReturn(template);

        // when
        EmailTemplate updatedTemplate = emailTemplateService.updateEmailTemplate(id, newSubject, newContent);

        // then
        assertThat(updatedTemplate.getSubject()).isEqualTo(newSubject);
        assertThat(updatedTemplate.getContent()).isEqualTo(newContent);
        verify(emailTemplateRepository, times(1)).save(template);
    }

    @Test
    void getTemplateByName_shouldReturnTemplate() {
        // given
        String name = "welcome";
        EmailTemplate template = new EmailTemplate();
        template.setName(name);
        given(emailTemplateRepository.findByName(name)).willReturn(Optional.of(template));

        // when
        EmailTemplate result = emailTemplateService.getTemplateByName(name);

        // then
        assertThat(result).isEqualTo(template);
        verify(emailTemplateRepository, times(1)).findByName(name);
    }

    @Test
    void getTemplateByName_shouldThrowExceptionWhenNotFound() {
        // given
        String name = "nonexistent";
        given(emailTemplateRepository.findByName(name)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> emailTemplateService.getTemplateByName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email template not found with name: " + name);
        verify(emailTemplateRepository, times(1)).findByName(name);
    }
}
