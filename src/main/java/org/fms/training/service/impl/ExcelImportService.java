package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.fms.training.common.entity.*;
import org.fms.training.common.enums.Status;
import org.fms.training.repository.*;
import org.fms.training.service.ImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExcelImportService implements ImportService {

    private final TopicRepository topicRepository;
    private final TopicAssessmentRepository topicAssessmentRepository;
    private final TechnicalGroupRepository technicalGroupRepository;
    private final UnitRepository unitRepository;
    private final UnitSectionRepository unitSectionRepository;

    /**
     * Import data from Excel InputStream with optional confirmation for updates.
     *
     * @param inputStream   Input stream of the Excel file.
     * @param confirmUpdate Boolean indicating if update is confirmed by the user.
     */
    @Transactional
    public void importDataFromStream(InputStream inputStream, boolean confirmUpdate) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            // Import Syllabus sheet
            Sheet syllabusSheet = workbook.getSheet("Syllabus");
            if (syllabusSheet == null) {
                throw new IllegalArgumentException("Sheet 'Syllabus' not found.");
            }

            Topic savedTopic = importSyllabusSheet(syllabusSheet, confirmUpdate);

            // Import ScheduleDetail sheet
            Sheet scheduleDetailSheet = workbook.getSheet("ScheduleDetail");
            if (scheduleDetailSheet == null) {
                throw new IllegalArgumentException("Sheet 'ScheduleDetail' not found.");
            }

            // Proceed to import only if topic update is confirmed
            importScheduleDetailSheet(scheduleDetailSheet, savedTopic);

        }catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error while importing Excel data: " + e.getMessage());
            throw new RuntimeException("Failed to import Excel file: " + e.getMessage(), e);
        }
    }

    /**
     * Import data from the Syllabus sheet. If the topic exists and update is not confirmed, throw an error.
     */
    protected Topic importSyllabusSheet(Sheet sheet, boolean confirmUpdate) {
        Row row = sheet.getRow(1);
        String technicalGroupCode = row.getCell(2).getStringCellValue();

        TechnicalGroup technicalGroup = technicalGroupRepository.findByCode(technicalGroupCode)
                .orElseThrow(() -> new IllegalArgumentException("Technical group not found with code: " + technicalGroupCode));

        String topicName = sheet.getRow(2).getCell(2).getStringCellValue();
        if (topicName == null || topicName.isEmpty()) {
            throw new IllegalArgumentException("Topic Name is missing.");
        }
        String topicCode = sheet.getRow(3).getCell(2).getStringCellValue();
        if (topicCode == null || topicCode.isEmpty()) {
            throw new IllegalArgumentException("Topic Code is missing.");
        }
        String version = getCellValueAsString(sheet.getRow(4).getCell(2));
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("Version is missing.");
        }
        String passCriteria = getCellValueAsString(sheet.getRow(9).getCell(3));
        if (passCriteria == null || passCriteria.isEmpty()) {
            throw new IllegalArgumentException("Pass Criteria is missing.");
        }

        Optional<Topic> existingTopicOpt = topicRepository.findByTopicCodeAndVersion(topicCode, version);

        Topic topic;
        if (existingTopicOpt.isPresent()) {
            if (!confirmUpdate) {
                throw new IllegalStateException("Topic with the same code and version already exists. Please confirm to update.");
            }
            // If confirmed, delete related data to prevent stale entity issues
            deleteTopicData(existingTopicOpt.get());
            topicRepository.delete(existingTopicOpt.get());
        }

        // Create new Topic or use the existing topic if found
        topic = new Topic();
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
        for (int rowIndex = 5; rowIndex <= 8; rowIndex++) {
            row = sheet.getRow(rowIndex);
            if (row != null) {
                importTopicAssessment(row, savedTopic);
            }
        }

        return savedTopic;
    }

    /**
     * Delete all related data of a topic to avoid stale entity issues.
     */
    @Transactional
    protected void deleteTopicData(Topic topic) {
        // Delete units and their sections
        List<Unit> units = unitRepository.findByTopic(topic);
        for (Unit unit : units) {
            unitSectionRepository.deleteByUnit(unit);
        }
        unitRepository.deleteAll(units);

        // Delete topic assessments
        topicAssessmentRepository.deleteByTopic(topic);
    }

    /**
     * Import a TopicAssessment from a row and save it to the database.
     */
    private void importTopicAssessment(Row row, Topic topic) {
        String assessmentName = getCellValueAsString(row.getCell(2));
        if (assessmentName == null || assessmentName.isEmpty()) {
            return;
        }

        TopicAssessment topicAssessment = new TopicAssessment();
        topicAssessment.setAssessmentName(assessmentName);

        Cell quantityCell = row.getCell(3);
        if (quantityCell != null && quantityCell.getCellType() == CellType.NUMERIC) {
            topicAssessment.setQuantity((int) quantityCell.getNumericCellValue());
        }

        Cell weightedNumberCell = row.getCell(4);
        if (weightedNumberCell != null && weightedNumberCell.getCellType() == CellType.NUMERIC) {
            topicAssessment.setWeightedNumber((int) weightedNumberCell.getNumericCellValue());
        }

        topicAssessment.setNote(getCellValueAsString(row.getCell(5)));
        topicAssessment.setTopic(topic);

        topicAssessmentRepository.save(topicAssessment);
    }

    /**
     * Import data from the ScheduleDetail sheet and save units and sections.
     */
    protected void importScheduleDetailSheet(Sheet sheet, Topic topic) {
        List<Unit> unitsToSave = new ArrayList<>();
        int unitNumber = 1;
        Unit currentUnit = null;
        int sectionNumberCounter = 1;

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                String unitName = getCellValueAsString(row.getCell(1));
                if (unitName != null && !unitName.trim().isEmpty()) {
                    if (currentUnit != null) {
                        unitsToSave.add(currentUnit);
                    }
                    sectionNumberCounter = 1;

                    currentUnit = new Unit();
                    currentUnit.setUnitName(unitName);
                    currentUnit.setUnitNumber(unitNumber++);
                    currentUnit.setTopic(topic);
                    currentUnit.setUnitSections(new ArrayList<>());
                }

                if (currentUnit != null) {
                    UnitSection unitSection = createUnitSection(row, currentUnit, sectionNumberCounter++);
                    currentUnit.getUnitSections().add(unitSection);
                }
            }
        }

        if (currentUnit != null) {
            unitsToSave.add(currentUnit);
        }

        unitRepository.saveAll(unitsToSave);
    }

    /**
     * Create a new UnitSection from a row.
     */
    private UnitSection createUnitSection(Row row, Unit unit, int sectionNumber) {
        UnitSection unitSection = new UnitSection();
        unitSection.setUnit(unit);
        unitSection.setTitle(getCellValueAsString(row.getCell(3)));
        unitSection.setDescription(getCellValueAsString(row.getCell(2)));
        unitSection.setDeliveryType(getCellValueAsString(row.getCell(4)));

        Double duration = getCellValueAsDouble(row.getCell(5));
        if (duration == null) {
            throw new IllegalArgumentException("Duration is missing or invalid at row: " + row.getRowNum());
        }
        unitSection.setDuration(duration);

        unitSection.setTrainingFormat(getCellValueAsString(row.getCell(6)));
        unitSection.setNote(getCellValueAsString(row.getCell(7)));
        unitSection.setSectionNumber(sectionNumber);

        return unitSection;
    }

    public String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    protected Double getCellValueAsDouble(Cell cell) {
        if (cell == null) return null;
        try {
            return cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : Double.parseDouble(cell.getStringCellValue());
        } catch (NumberFormatException e) {
            System.err.println("Cannot parse double from: " + cell);
            return null;
        }
    }
}
