package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.fms.training.entity.TechnicalGroup;
import org.fms.training.entity.Topic;
import org.fms.training.entity.TopicAssessment;
import org.fms.training.enums.Status;
import org.fms.training.repository.TechnicalGroupRepository;
import org.fms.training.repository.TopicAssessmentRepository;
import org.fms.training.repository.TopicRepository;
import org.fms.training.service.ImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExcelImportService implements ImportService {

    private final TopicRepository topicRepository;
    private final TopicAssessmentRepository topicAssessmentRepository;
    private final TechnicalGroupRepository technicalGroupRepository;

    @Transactional
    public void importDataFromFile(File excelFile) {
        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet("Syllabus");
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'Syllabus' not found");
            }

            // Reading the Topic data from the excel
            Row row = sheet.getRow(1); // Row 1 for Technical Group
            String technicalGroupCode = row.getCell(1).getStringCellValue();

            // Fetch or Create Technical Group
            TechnicalGroup technicalGroup = technicalGroupRepository.findByCode(technicalGroupCode)
                    .orElseGet(() -> {
                        TechnicalGroup newTechnicalGroup = new TechnicalGroup();
                        newTechnicalGroup.setCode(technicalGroupCode);
                        return technicalGroupRepository.save(newTechnicalGroup); // Save and return new Technical Group
                    });

            String topicName = sheet.getRow(2).getCell(1).getStringCellValue();  // Row 2 for Topic Name
            String topicCode = sheet.getRow(3).getCell(1).getStringCellValue();  // Row 3 for Topic Code
            String version = sheet.getRow(4).getCell(1).getStringCellValue();    // Row 4 for Version
            String passCriteria = sheet.getRow(10).getCell(1).getStringCellValue(); // Row 10 for Pass Criteria

            // Create and save the Topic entity
            Topic topic = new Topic();
            topic.setTechnicalGroup(technicalGroup);  // Set the Technical Group object (with ID)
            topic.setTopicName(topicName);
            topic.setTopicCode(topicCode);
            topic.setVersion(version);
            topic.setPassCriteria(passCriteria);
            topic.setStatus(Status.ACTIVE);
            topic.setLastModifiedDate(LocalDateTime.now());
            topic.setLastModifiedBy("admin");

            Topic savedTopic = topicRepository.save(topic);

            // Insert into TopicAssessment based on Assessment Scheme
            Row assessmentRow = sheet.getRow(10); // Row 10 for assessments
            TopicAssessment topicAssessment = new TopicAssessment();
            topicAssessment.setAssessmentName("Final Report");
            topicAssessment.setQuantity(1); // Assuming this is a fixed value
            topicAssessment.setWeightedNumber(100); // Assuming 100% is fixed
            topicAssessment.setTopic(savedTopic);

            topicAssessmentRepository.save(topicAssessment);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to import Excel file: " + e.getMessage());
        }
    }
}
