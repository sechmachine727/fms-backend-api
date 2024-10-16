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
import java.util.*;

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
        Topic topic = topicRepository.findByTopicCodeAndVersion(topicCode, version)
                .orElseGet(() -> new Topic());

        // Validate and get Pass Criteria (Row 9, Column 3)
        Cell passCriteriaCell = sheet.getRow(9).getCell(3);
        String passCriteria = getCellValueAsString(passCriteriaCell);
        if (passCriteria == null || passCriteria.isEmpty()) {
            throw new IllegalArgumentException("Pass Criteria is missing.");
        }

        // Create and save the Topic entity
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

        if (topic.getId() != null) {
            topicAssessmentRepository.deleteByTopic(topic);
        }

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



    protected void importScheduleDetailSheet(Sheet sheet, Topic topic) {
        // Lấy tất cả các Units hiện có của topic từ DB
        List<Unit> existingUnits = unitRepository.findByTopic(topic);
        Map<String, Unit> unitMap = new HashMap<>(); // Lưu theo unit name để so sánh nhanh

        for (Unit unit : existingUnits) {
            unitMap.put(unit.getUnitName(), unit); // Lưu Unit hiện tại để so sánh
        }

        // Danh sách các Units và Unit Sections mới từ file Excel
        List<Unit> unitsToSave = new ArrayList<>();
        Set<Integer> unitSectionIdsToKeep = new HashSet<>(); // Để theo dõi UnitSection không bị xóa

        int unitNumber = 1;
        Unit currentUnit = null;
        int sectionNumberCounter = 1;

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                String unitName = getCellValueAsString(row.getCell(1));
                if (unitName != null && !unitName.trim().isEmpty()) {
                    // Nếu đã có currentUnit, thêm nó vào danh sách để lưu lại
                    if (currentUnit != null) {
                        unitsToSave.add(currentUnit);
                    }

                    // Reset section number counter khi bắt đầu một Unit mới
                    sectionNumberCounter = 1;

                    // Kiểm tra nếu Unit đã tồn tại thì xóa hết UnitSections trước khi thêm mới
                    currentUnit = unitMap.getOrDefault(unitName, new Unit());
                    currentUnit.setUnitName(unitName);
                    currentUnit.setUnitNumber(unitNumber++);
                    currentUnit.setTopic(topic);

                    // Xóa tất cả UnitSections của Unit trước khi thêm mới
                    unitSectionRepository.deleteByUnit(currentUnit);
                    currentUnit.setUnitSections(new ArrayList<>());  // Reset lại danh sách UnitSections
                }

                if (currentUnit != null) {
                    String title = getCellValueAsString(row.getCell(3));
                    String description = getCellValueAsString(row.getCell(2));

                    // Tạo mới UnitSection cho Unit hiện tại
                    UnitSection unitSection = new UnitSection();
                    unitSection.setUnit(currentUnit); // Gắn vào Unit hiện tại
                    unitSection.setTitle(title);   // Learning Objectives
                    unitSection.setDescription(description);  // Content
                    unitSection.setDeliveryType(getCellValueAsString(row.getCell(4))); // Delivery Type
                    Double durationValue = getCellValueAsDouble(row.getCell(5));  // Duration
                    if (durationValue != null) {
                        unitSection.setDuration(durationValue);
                    } else {
                        throw new IllegalArgumentException("Duration value is missing or invalid at row: " + rowIndex);
                    }
                    unitSection.setTrainingFormat(getCellValueAsString(row.getCell(6))); // Training Format
                    unitSection.setNote(getCellValueAsString(row.getCell(7)));           // Notes

                    // Gán giá trị cho sectionNumber
                    unitSection.setSectionNumber(sectionNumberCounter++);

                    // Thêm UnitSection mới vào danh sách
                    currentUnit.getUnitSections().add(unitSection);
                }
            }
        }

// Lưu Unit cuối cùng nếu có
        if (currentUnit != null) {
            unitsToSave.add(currentUnit);
        }

// Lưu tất cả các Units với các UnitSections mới
        unitRepository.saveAll(unitsToSave);

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
