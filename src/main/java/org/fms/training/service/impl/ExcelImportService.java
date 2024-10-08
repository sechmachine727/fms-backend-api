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
            throw new RuntimeException("Failed to import Excel file: " + e.getMessage());
        }
    }

    private Topic importSyllabusSheet(Sheet sheet) {
        // Reading the Topic data from the excel
        Row row = sheet.getRow(1); // Row 1 for Technical Group
        String technicalGroupCode = row.getCell(2).getStringCellValue();

        // Validate technicalGroupCode
        if (technicalGroupCode == null || technicalGroupCode.isEmpty()) {
            throw new IllegalArgumentException("Technical Group Code is missing.");
        }

        // Fetch or Create Technical Group
        TechnicalGroup technicalGroup = technicalGroupRepository.findByCode(technicalGroupCode)
                .orElseThrow(() -> new IllegalArgumentException("Technical group not found with code: " + technicalGroupCode));

        // Validate and get Topic Name (Row 2, Column 2)
        String topicName = sheet.getRow(2).getCell(2).getStringCellValue();
        if (topicName == null || topicName.isEmpty()) {
            throw new IllegalArgumentException("Topic Name is missing.");
        }

        // Validate and get Topic Code (Row 3, Column 2)
        String topicCode = sheet.getRow(3).getCell(2).getStringCellValue();
        if (topicCode == null || topicCode.isEmpty()) {
            throw new IllegalArgumentException("Topic Code is missing.");
        }

        // Handling version (Row 4, Column 2) and validate
        Cell versionCell = sheet.getRow(4).getCell(2);
        String version = getCellValueAsString(versionCell);
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("Version is missing.");
        }

        // Check if Topic Code with the same version already exists
        Optional<Topic> existingTopic = topicRepository.findByTopicCodeAndVersion(topicCode, version);
        if (existingTopic.isPresent()) {
            throw new IllegalArgumentException("Topic with code " + topicCode + " and version " + version + " already exists. Please change the version or topic code.");
        }

        // Validate and get Pass Criteria (Row 9, Column 3)
        Cell passCriteriaCell = sheet.getRow(9).getCell(3);
        String passCriteria = getCellValueAsString(passCriteriaCell);
        if (passCriteria == null || passCriteria.isEmpty()) {
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

        // Importing Topic Assessments
        for (int rowIndex = 5; rowIndex <= 8; rowIndex++) {
            row = sheet.getRow(rowIndex);

            if (row != null) {
                Cell assessmentNameCell = row.getCell(2);
                Cell quantityCell = row.getCell(3);
                Cell weightedNumberCell = row.getCell(4);
                Cell noteCell = row.getCell(5);

                if (assessmentNameCell != null && assessmentNameCell.getCellType() == CellType.STRING) {
                    String assessmentName = assessmentNameCell.getStringCellValue();

                    if (!assessmentName.isEmpty()) {
                        TopicAssessment topicAssessment = new TopicAssessment();
                        topicAssessment.setAssessmentName(assessmentName);

                        // Set quantity
                        if (quantityCell != null && quantityCell.getCellType() == CellType.NUMERIC) {
                            topicAssessment.setQuantity((int) quantityCell.getNumericCellValue());
                        }

                        // Set weighted number
                        if (weightedNumberCell != null && weightedNumberCell.getCellType() == CellType.NUMERIC) {
                            double weightedNumber = weightedNumberCell.getNumericCellValue();
                            topicAssessment.setWeightedNumber((int) weightedNumber);
                        }

                        // Set note
                        if (noteCell != null && noteCell.getCellType() == CellType.STRING) {
                            topicAssessment.setNote(noteCell.getStringCellValue());
                        }

                        // Associate the Topic and save TopicAssessment
                        topicAssessment.setTopic(savedTopic);
                        topicAssessmentRepository.save(topicAssessment);
                    }
                }
            }
        }
        return savedTopic;
    }

    private void importScheduleDetailSheet(Sheet sheet, Topic topic) {
        int unitNumber = 1; // Start unit number from 1
        Unit currentUnit = null;

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            if (row != null) {
                // Extract the Unit name from column 1 (Training Unit/Chapter)
                Cell unitNameCell = row.getCell(1);
                String unitName = getCellValueAsString(unitNameCell);

                // Kiểm tra xem Unit Name có khác rỗng và không trùng với Unit hiện tại
                if (unitName != null && !unitName.trim().isEmpty()) {
                    // Nếu có Unit hiện tại thì lưu lại trước khi tạo Unit mới
                    if (currentUnit != null) {
                        unitRepository.save(currentUnit); // Save the current unit
                    }

                    // Tạo Unit mới
                    currentUnit = new Unit();
                    currentUnit.setUnitName(unitName);
                    currentUnit.setUnitNumber(unitNumber++);
                    currentUnit.setTopic(topic); // Associate the Unit with the Topic

                    // Khởi tạo danh sách UnitSections để tránh null pointer exception
                    currentUnit.setUnitSections(new ArrayList<>());

                    unitRepository.save(currentUnit);
                }

                // Đảm bảo rằng currentUnit không null trước khi thêm UnitSection
                if (currentUnit != null) {
                    // Tạo và set UnitSection data
                    UnitSection unitSection = new UnitSection();
                    unitSection.setTitle(getCellValueAsString(row.getCell(3)));         // Learning Objectives
                    unitSection.setDescription(getCellValueAsString(row.getCell(2)));   // Content
                    unitSection.setDeliveryType(getCellValueAsString(row.getCell(4)));  // Delivery Type
                    Double durationValue = getCellValueAsDouble(row.getCell(5));        // Duration
                    if (durationValue != null) {
                        unitSection.setDuration(durationValue);  // Set giá trị Duration nếu hợp lệ
                    } else {
                        throw new IllegalArgumentException("Duration value is missing or invalid at row: " + rowIndex);
                    }
                    unitSection.setTrainingFormat(getCellValueAsString(row.getCell(6))); // Training Format
                    unitSection.setNote(getCellValueAsString(row.getCell(7)));          // Notes
                    unitSection.setSectionNumber(1);  // Default Section Number

                    // Liên kết UnitSection với Unit hiện tại
                    unitSection.setUnit(currentUnit);

                    // Thêm UnitSection vào danh sách của Unit
                    currentUnit.getUnitSections().add(unitSection);

                    // Lưu UnitSection vào cơ sở dữ liệu
                    unitSectionRepository.save(unitSection);
                }
            }
        }

        // Lưu lại Unit cuối cùng nếu có
        if (currentUnit != null) {
            unitRepository.save(currentUnit);
        }
    }


    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private Double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return null;
        }

        // Kiểm tra loại dữ liệu của ô
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                System.err.println("Cannot parse double from string: " + cell.getStringCellValue());
                return null;
            }
        } else {
            return null;
        }
    }

}
