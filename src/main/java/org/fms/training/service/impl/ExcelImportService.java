package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
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
            String technicalGroupCode = row.getCell(2).getStringCellValue();

            // Fetch or Create Technical Group
            TechnicalGroup technicalGroup = technicalGroupRepository.findByCode(technicalGroupCode)
                    .orElseThrow(() -> new IllegalArgumentException("Technical group not found with code: " + technicalGroupCode));

            String topicName = sheet.getRow(2).getCell(2).getStringCellValue();  // Row 2 for Topic Name
            String topicCode = sheet.getRow(3).getCell(2).getStringCellValue();  // Row 3 for Topic Code
            Cell versionCell = sheet.getRow(4).getCell(2);
            String version;

            if (versionCell.getCellType() == CellType.STRING) {
                version = versionCell.getStringCellValue();  // Nếu là chuỗi
            } else if (versionCell.getCellType() == CellType.NUMERIC) {
                version = String.valueOf((int) versionCell.getNumericCellValue());  // Nếu là số, chuyển đổi thành chuỗi
            } else {
                throw new IllegalStateException("Unexpected cell type for version");
            }

            // Lấy giá trị của Pass Criteria (Row 9, Column 3)
            Cell passCriteriaCell = sheet.getRow(9).getCell(3);
            String passCriteria;

            if (passCriteriaCell.getCellType() == CellType.STRING) {
                passCriteria = passCriteriaCell.getStringCellValue();  // Nếu là chuỗi
            } else if (passCriteriaCell.getCellType() == CellType.NUMERIC) {
                passCriteria = String.valueOf((int) passCriteriaCell.getNumericCellValue());  // Nếu là số, chuyển đổi thành chuỗi
            } else {
                throw new IllegalStateException("Unexpected cell type for pass criteria");
            }

            // Create and save the Topic entity
            Topic topic = new Topic();
            topic.setTechnicalGroup(technicalGroup);
            topic.setTopicName(topicName);
            topic.setTopicCode(topicCode);
            topic.setVersion(version);
            topic.setPassCriteria(passCriteria);
            topic.setDescription("description");
            topic.setStatus(Status.ACTIVE);
            topic.setLastModifiedDate(LocalDateTime.now());
            topic.setLastModifiedBy("admin");

            Topic savedTopic = topicRepository.save(topic);

            for (int rowIndex = 5; rowIndex <= 8; rowIndex++) {
                row = sheet.getRow(rowIndex);

                if (row != null) {
                    Cell assessmentNameCell = row.getCell(2);  // Cell 2 (index 1)
                    Cell quantityCell = row.getCell(3);        // Cell 3 (index 2)
                    Cell weightedNumberCell = row.getCell(4);  // Cell 4 (index 3)
                    Cell noteCell = row.getCell(5);            // Cell 5

                    if (assessmentNameCell != null && assessmentNameCell.getCellType() == CellType.STRING) {
                        String assessmentName = assessmentNameCell.getStringCellValue();

                        // Đảm bảo ô không rỗng và có giá trị
                        if (!assessmentName.isEmpty()) {
                            TopicAssessment topicAssessment = new TopicAssessment();
                            topicAssessment.setAssessmentName(assessmentName);

                            // Lấy giá trị quantity từ Cell 3 (index 2)
                            if (quantityCell != null) {
                                if (quantityCell.getCellType() == CellType.NUMERIC) {
                                    topicAssessment.setQuantity((int) quantityCell.getNumericCellValue());  // Set quantity
                                }
                            }

                            // Lấy giá trị weightedNumber từ Cell 4 (index 3)
                            if (weightedNumberCell != null) {
                                if (weightedNumberCell.getCellType() == CellType.NUMERIC) {
                                    double weightedNumber = weightedNumberCell.getNumericCellValue() * 100;
                                    topicAssessment.setWeightedNumber((int) weightedNumber);
                                }
                            }

                            if (noteCell != null && noteCell.getCellType() == CellType.STRING) {
                                String note = noteCell.getStringCellValue(); // Lấy giá trị note
                                topicAssessment.setNote(note); // Set note vào TopicAssessment
                            }

                            // Gán Topic đã lưu vào TopicAssessment
                            topicAssessment.setTopic(savedTopic);

                            // Lưu TopicAssessment vào cơ sở dữ liệu
                            topicAssessmentRepository.save(topicAssessment);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to import Excel file: " + e.getMessage());
        }
    }
}
