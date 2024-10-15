package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.fms.training.entity.*;
import org.fms.training.enums.Status;
import org.fms.training.repository.*;
import org.fms.training.service.ImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExcelImportService implements ImportService {

    private final TopicRepository topicRepository;
    private final TopicAssessmentRepository topicAssessmentRepository;
    private final TechnicalGroupRepository technicalGroupRepository;
    private final UnitRepository unitRepository;
    private final UnitSectionRepository unitSectionRepository;

    @Transactional
    public void importDataFromStream(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            // Import data from the "Syllabus" sheet
            Sheet syllabusSheet = workbook.getSheet("Syllabus");
            if (syllabusSheet == null) {
                throw new IllegalArgumentException("Sheet 'Syllabus' not found");
            }

            Topic savedTopic = importSyllabusSheet(syllabusSheet);

            // Import data from the "ScheduleDetail" sheet
            Sheet scheduleDetailSheet = workbook.getSheet("ScheduleDetail");
            if (scheduleDetailSheet == null) {
                throw new IllegalArgumentException("Sheet 'ScheduleDetail' not found");
            }

            importScheduleDetailSheet(scheduleDetailSheet, savedTopic);

        } catch (Exception e) {
            // Log the error and rethrow exception with detailed message
            System.err.println("Error while importing Excel data: " + e.getMessage());
            throw new RuntimeException("Failed to import Excel file: " + e.getMessage(), e);
        }
    }

    protected Topic importSyllabusSheet(Sheet sheet) {
        // Validate and get Technical Group Code (Row 1, Column 2)
        String technicalGroupCode = getCellValueAsString(sheet.getRow(1), 2);
        if (technicalGroupCode == null || technicalGroupCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Technical Group Code is missing.");
        }

        // Fetch Technical Group
        TechnicalGroup technicalGroup = technicalGroupRepository.findByCode(technicalGroupCode)
                .orElseThrow(() -> new IllegalArgumentException("Technical group not found with code: " + technicalGroupCode));

        // Validate and get Topic Name (Row 2, Column 2)
        String topicName = getCellValueAsString(sheet.getRow(2), 2);
        if (topicName == null || topicName.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic Name is missing.");
        }

        // Validate and get Topic Code (Row 3, Column 2)
        String topicCode = getCellValueAsString(sheet.getRow(3), 2);
        if (topicCode == null || topicCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic Code is missing.");
        }

        // Validate and get Version (Row 4, Column 2)
        String version = getCellValueAsString(sheet.getRow(4), 2);
        if (version == null || version.trim().isEmpty()) {
            throw new IllegalArgumentException("Version is missing.");
        }

        // Check if Topic with same code and version already exists
        Optional<Topic> existingTopic = topicRepository.findByTopicCodeAndVersion(topicCode, version);
        if (existingTopic.isPresent()) {
            throw new IllegalArgumentException("Topic with code " + topicCode + " and version " + version + " already exists.");
        }

        // Validate and get Pass Criteria (Row 9, Column 3)
        String passCriteria = getCellValueAsString(sheet.getRow(9), 3);
        if (passCriteria == null || passCriteria.trim().isEmpty()) {
            throw new IllegalArgumentException("Pass Criteria is missing.");
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

        // Import Topic Assessments
        importTopicAssessments(sheet, savedTopic);

        return savedTopic;
    }

    protected void importTopicAssessments(Sheet sheet, Topic savedTopic) {
        for (int rowIndex = 5; rowIndex <= 8; rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            if (row != null) {
                String assessmentName = getCellValueAsString(row, 2);
                if (assessmentName != null && !assessmentName.trim().isEmpty()) {
                    TopicAssessment topicAssessment = new TopicAssessment();
                    topicAssessment.setAssessmentName(assessmentName);

                    // Set quantity
                    Double quantity = getCellValueAsDouble(row.getCell(3));
                    if (quantity != null) {
                        topicAssessment.setQuantity(quantity.intValue());
                    }

                    // Set weighted number
                    Double weightedNumber = getCellValueAsDouble(row.getCell(4));
                    if (weightedNumber != null) {
                        topicAssessment.setWeightedNumber(weightedNumber.intValue());
                    }

                    // Set note
                    String note = getCellValueAsString(row, 5);
                    if (note != null) {
                        topicAssessment.setNote(note);
                    }

                    // Associate the Topic and save TopicAssessment
                    topicAssessment.setTopic(savedTopic);
                    topicAssessmentRepository.save(topicAssessment);
                }
            }
        }
    }

    protected void importScheduleDetailSheet(Sheet sheet, Topic topic) {
        int unitNumber = 1;
        Unit currentUnit = null;

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            if (row != null) {
                // Extract the Unit name from column 1 (Training Unit/Chapter)
                String unitName = getCellValueAsString(row, 1);

                if (unitName != null && !unitName.trim().isEmpty()) {
                    if (currentUnit != null) {
                        unitRepository.save(currentUnit);
                    }

                    currentUnit = new Unit();
                    currentUnit.setUnitName(unitName);
                    currentUnit.setUnitNumber(unitNumber++);
                    currentUnit.setTopic(topic);
                    currentUnit.setUnitSections(new ArrayList<>());

                    unitRepository.save(currentUnit);
                }

                if (currentUnit != null) {
                    UnitSection unitSection = new UnitSection();
                    unitSection.setTitle(getCellValueAsString(row, 3));
                    unitSection.setDescription(getCellValueAsString(row, 2));
                    unitSection.setDeliveryType(getCellValueAsString(row, 4));
                    unitSection.setDuration(getCellValueAsDouble(row.getCell(5)));
                    unitSection.setTrainingFormat(getCellValueAsString(row, 6));
                    unitSection.setNote(getCellValueAsString(row, 7));
                    unitSection.setSectionNumber(1);

                    unitSection.setUnit(currentUnit);
                    currentUnit.getUnitSections().add(unitSection);

                    unitSectionRepository.save(unitSection);
                }
            }
        }

        if (currentUnit != null) {
            unitRepository.save(currentUnit);
        }
    }

    protected String getCellValueAsString(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return getCellValueAsString(cell);
    }

    protected String getCellValueAsString(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    protected Double getCellValueAsDouble(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                System.err.println("Cannot parse double from string: " + cell.getStringCellValue());
            }
        }
        return null;
    }
}
