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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
            topic = existingTopicOpt.get();
            if (!confirmUpdate) {
                throw new IllegalStateException("Topic with the same code and version already exists. Please confirm to update.");
            }

            // Update existing topic
            topic.setTechnicalGroup(technicalGroup);
            topic.setTopicName(topicName);
            topic.setPassCriteria(passCriteria);
            topic.setLastModifiedDate(LocalDateTime.now());
            topic.setLastModifiedBy("admin");

            // Update related Topic Assessments, Units, and Unit Sections
            updateTopicAssessments(sheet, topic);
        } else {
            // Create new topic
            topic = new Topic();
            topic.setTechnicalGroup(technicalGroup);
            topic.setTopicName(topicName);
            topic.setTopicCode(topicCode);
            topic.setVersion(version);
            topic.setPassCriteria(passCriteria);
            topic.setStatus(Status.ACTIVE);
            topic.setLastModifiedDate(LocalDateTime.now());
            topic.setLastModifiedBy("admin");

            topicRepository.save(topic);
        }

        return topic;
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
    @Transactional
    protected void updateTopicAssessments(Sheet sheet, Topic topic) {
        List<TopicAssessment> existingAssessments = topicAssessmentRepository.findByTopic(topic);

        // Iterate through each row for Topic Assessments
        for (int rowIndex = 5; rowIndex <= 8; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                String assessmentName = getCellValueAsString(row.getCell(2));
                if (assessmentName != null && !assessmentName.isEmpty()) {
                    TopicAssessment existingAssessment = existingAssessments.stream()
                            .filter(ta -> ta.getAssessmentName().equals(assessmentName))
                            .findFirst()
                            .orElse(new TopicAssessment());

                    existingAssessment.setAssessmentName(assessmentName);
                    existingAssessment.setQuantity((int) row.getCell(3).getNumericCellValue());
                    existingAssessment.setWeightedNumber((int) row.getCell(4).getNumericCellValue());
                    existingAssessment.setTopic(topic);

                    topicAssessmentRepository.save(existingAssessment);
                }
            }
        }
    }

    /**
     * Import data from the ScheduleDetail sheet and save units and sections.
     */
    @Transactional
    protected void importScheduleDetailSheet(Sheet sheet, Topic topic) {
        List<Unit> unitsToSave = new ArrayList<>();  // List of Units to save
        int unitNumber = 1;  // Unit counter
        Unit currentUnit = null;  // Current Unit being processed
        int sectionNumberCounter = 1;  // UnitSection counter

        // Iterate through all rows in the sheet
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                // Get the Unit name from column 1
                String unitName = getCellValueAsString(row.getCell(1));

                // Check if the row indicates a new Unit (non-empty and different from the current one)
                if (unitName != null && !unitName.trim().isEmpty()) {
                    // If a previous Unit exists, save it before starting a new one
                    if (currentUnit != null) {
                        unitsToSave.add(currentUnit);
                    }

                    // Create a new Unit
                    currentUnit = new Unit();
                    currentUnit.setUnitName(unitName);
                    currentUnit.setUnitNumber(unitNumber++);
                    currentUnit.setTopic(topic);
                    currentUnit.setUnitSections(new ArrayList<>());  // Initialize UnitSections list

                    // Reset the UnitSection counter for the new Unit
                    sectionNumberCounter = 1;
                }

                // If there's an active Unit, add a new UnitSection to it
                if (currentUnit != null) {
                    UnitSection unitSection = createOrUpdateUnitSection(row, currentUnit, sectionNumberCounter++);
                    currentUnit.getUnitSections().add(unitSection);  // Add to the Unit's sections list
                }
            }
        }

        // Save the last Unit if it exists
        if (currentUnit != null) {
            unitsToSave.add(currentUnit);
        }

        // Save all Units and their UnitSections to the database
        unitRepository.saveAll(unitsToSave);
    }






    /**
     * Create a new UnitSection from a row.
     */
    private UnitSection createOrUpdateUnitSection(Row row, Unit unit, int sectionNumber) {
        String sectionTitle = getCellValueAsString(row.getCell(3));
        String description = getCellValueAsString(row.getCell(2));
        String deliveryType = getCellValueAsString(row.getCell(4));
        Double duration = getCellValueAsDouble(row.getCell(5));
        String trainingFormat = getCellValueAsString(row.getCell(6));
        String note = getCellValueAsString(row.getCell(7));

        // Find existing section or create a new one
        UnitSection unitSection = unit.getUnitSections().stream()
                .filter(us -> us.getTitle().equals(sectionTitle) &&
                        us.getDescription().equals(description) &&
                        us.getDeliveryType().equals(deliveryType) &&
                        us.getDuration().equals(duration) &&
                        us.getTrainingFormat().equals(trainingFormat) &&
                        (us.getNote() == null ? note == null : us.getNote().equals(note)) &&
                        us.getSectionNumber() == sectionNumber)
                .findFirst()
                .orElse(new UnitSection());

        unitSection.setUnit(unit);
        unitSection.setTitle(sectionTitle);
        unitSection.setDescription(description);
        unitSection.setDeliveryType(deliveryType);
        unitSection.setDuration(duration);
        unitSection.setTrainingFormat(trainingFormat);
        unitSection.setNote(note);
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
