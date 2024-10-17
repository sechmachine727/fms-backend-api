package org.fms.training.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.fms.training.common.entity.*;
import org.fms.training.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExcelImportServiceTest {

    @InjectMocks
    private ExcelImportService excelImportService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicAssessmentRepository topicAssessmentRepository;

    @Mock
    private TechnicalGroupRepository technicalGroupRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private UnitSectionRepository unitSectionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void importDataFromStream_shouldImportDataSuccessfully() {
        // Arrange
        InputStream excelStream = getClass().getResourceAsStream("/Template_Import_Syllabus.xlsx"); // A sample Excel file should be placed under the test resources
        TechnicalGroup mockTechnicalGroup = new TechnicalGroup();
        mockTechnicalGroup.setId(1);
        mockTechnicalGroup.setCode("TG001");

        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.of(mockTechnicalGroup));
        when(topicRepository.findByTopicCodeAndVersion(anyString(), anyString())).thenReturn(Optional.empty());

        // Act
        excelImportService.importDataFromStream(excelStream);

        // Assert
        verify(technicalGroupRepository, times(1)).findByCode(anyString());
        verify(topicRepository, times(1)).save(any(Topic.class));
        verify(topicAssessmentRepository, atLeastOnce()).save(any(TopicAssessment.class));
        verify(unitRepository, atLeastOnce()).save(any(Unit.class));
        verify(unitSectionRepository, atLeastOnce()).save(any(UnitSection.class));
    }

    @Test
    void importDataFromStream_shouldThrowExceptionWhenSyllabusSheetMissing() {
        // Arrange
        InputStream invalidExcelStream = getClass().getResourceAsStream("/SyllabusSheetMissing.xlsx"); // Missing "Syllabus" sheet

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            excelImportService.importDataFromStream(invalidExcelStream);
        });

        assertEquals("Failed to import Excel file: Sheet 'Syllabus' not found", exception.getMessage());
    }

    @Test
    void importDataFromStream_shouldThrowExceptionWhenTopicAlreadyExists() {
        // Arrange
        InputStream excelStream = getClass().getResourceAsStream("/Template_Import_Syllabus.xlsx");
        TechnicalGroup mockTechnicalGroup = new TechnicalGroup();
        mockTechnicalGroup.setId(1);
        mockTechnicalGroup.setCode("TG001");

        Topic existingTopic = new Topic();
        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.of(mockTechnicalGroup));
        when(topicRepository.findByTopicCodeAndVersion(anyString(), anyString())).thenReturn(Optional.of(existingTopic));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            excelImportService.importDataFromStream(excelStream);
        });

        assertEquals("Failed to import Excel file: Topic with code TEEST and version 4.1 already exists. Please change the version or topic code.", exception.getMessage());
    }

    @Test
    void importSyllabusSheet_shouldThrowExceptionWhenTechnicalGroupNotFound() throws Exception {
        // Arrange
        Workbook workbook = WorkbookFactory.create(Objects.requireNonNull(getClass().getResourceAsStream("/TechnicalGroupNotFound.xlsx")));
        Sheet syllabusSheet = workbook.getSheet("Syllabus");

        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            excelImportService.importSyllabusSheet(syllabusSheet);
        });

        assertEquals("Technical group not found with code: React Native", exception.getMessage());
    }

    @Test
    void importScheduleDetailSheet_shouldHandleScheduleDataCorrectly() throws Exception {
        // Arrange
        Workbook workbook = WorkbookFactory.create(Objects.requireNonNull(getClass().getResourceAsStream("/Template_Import_Syllabus.xlsx")));
        Sheet scheduleSheet = workbook.getSheet("ScheduleDetail");

        Topic mockTopic = new Topic();
        mockTopic.setId(1);
        mockTopic.setTopicCode("T001");
        mockTopic.setTopicName("Sample Topic");

        Unit savedUnit = new Unit();
        when(unitRepository.save(any(Unit.class))).thenReturn(savedUnit);

        // Act
        excelImportService.importScheduleDetailSheet(scheduleSheet, mockTopic);

        // Assert
        verify(unitRepository, atLeastOnce()).save(any(Unit.class));
        verify(unitSectionRepository, atLeastOnce()).save(any(UnitSection.class));
    }

    @Test
    void getCellValueAsString_shouldReturnNullForEmptyCells() {
        // Arrange
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.BLANK);

        // Act
        String result = excelImportService.getCellValueAsString(cell);

        // Assert
        assertNull(result);
    }

    @Test
    void getCellValueAsDouble_shouldHandleInvalidStringsGracefully() {
        // Arrange
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getStringCellValue()).thenReturn("InvalidNumber");

        // Act
        Double result = excelImportService.getCellValueAsDouble(cell);

        // Assert
        assertNull(result);
    }
}
