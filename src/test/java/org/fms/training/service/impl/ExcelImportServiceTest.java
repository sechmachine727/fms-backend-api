package org.fms.training.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.fms.training.common.entity.TechnicalGroup;
import org.fms.training.common.entity.Topic;
import org.fms.training.common.entity.TopicAssessment;
import org.fms.training.common.entity.Unit;
import org.fms.training.repository.TechnicalGroupRepository;
import org.fms.training.repository.TopicAssessmentRepository;
import org.fms.training.repository.TopicRepository;
import org.fms.training.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void importDataFromStream_shouldImportDataSuccessfully() {
        // Arrange
        InputStream excelStream = getClass().getResourceAsStream("/Template_Import_Syllabus.xlsx");
        TechnicalGroup mockTechnicalGroup = new TechnicalGroup();
        mockTechnicalGroup.setId(1);
        mockTechnicalGroup.setCode("TG001");

        // Mocking repositories
        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.of(mockTechnicalGroup));

        // Mocking empty topic result for new topic creation
        when(topicRepository.findByTopicCodeAndVersion(anyString(), anyString())).thenReturn(Optional.empty());

        // Mock existing units for the topic
        Unit mockUnit = new Unit();
        mockUnit.setId(1);
        List<Unit> mockUnits = Collections.singletonList(mockUnit);
        when(unitRepository.findByTopic(any(Topic.class))).thenReturn(mockUnits);

        // Act
        excelImportService.importDataFromStream(excelStream, false);

        // Assert
        verify(technicalGroupRepository, times(1)).findByCode(anyString());
        verify(topicRepository, atLeastOnce()).save(any(Topic.class));

        // Verify that topic assessments are saved correctly
        verify(topicAssessmentRepository, atLeastOnce()).save(any(TopicAssessment.class));

        // Verify that units and their sections are saved correctly
        verify(unitRepository, atLeastOnce()).saveAll(anyList());
    }


    @Test
    void importDataFromStream_shouldThrowExceptionWhenSyllabusSheetMissing() {
        // Arrange
        InputStream invalidExcelStream = getClass().getResourceAsStream("/SyllabusSheetMissing.xlsx"); // Missing "Syllabus" sheet

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            excelImportService.importDataFromStream(invalidExcelStream, false);
        });

        assertEquals("Failed to import Excel file: Sheet 'Syllabus' not found.", exception.getMessage());
    }


    @Test
    void importSyllabusSheet_shouldThrowExceptionWhenTechnicalGroupNotFound() throws Exception {
        // Arrange
        Workbook workbook = WorkbookFactory.create(Objects.requireNonNull(getClass().getResourceAsStream("/TechnicalGroupNotFound.xlsx")));
        Sheet syllabusSheet = workbook.getSheet("Syllabus");

        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            excelImportService.importSyllabusSheet(syllabusSheet, false);
        });

        assertEquals("Technical group not found with code: React Native", exception.getMessage());
    }


    @Test
    void getCellValueAsString_shouldReturnNullForEmptyCells() {
        // Arrange
        Cell cell = mock(Cell.class);
        when(cell.getCellType()).thenReturn(CellType.BLANK);

        // Act
        String result = excelImportService.getCellValueAsString(cell);

        // Assert
        assertEquals("", result);
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

    @Test
    void importDataFromStream_shouldThrowExceptionWhenScheduleDetailSheetMissing() {
        // Arrange
        InputStream excelStream = getClass().getResourceAsStream("/SyllabusSheetOnly.xlsx"); // A file without the "ScheduleDetail" sheet
        TechnicalGroup mockTechnicalGroup = new TechnicalGroup();
        mockTechnicalGroup.setId(1);
        mockTechnicalGroup.setCode("TG001");

        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.of(mockTechnicalGroup));
        when(topicRepository.findByTopicCodeAndVersion(anyString(), anyString())).thenReturn(Optional.empty());
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            excelImportService.importDataFromStream(excelStream, false);
        });

        assertEquals("Failed to import Excel file: Sheet 'ScheduleDetail' not found.", exception.getMessage());
    }


    @Test
    void importSyllabusSheet_shouldThrowExceptionWhenTopicCodeMissing() throws Exception {
        // Arrange
        Workbook workbook = WorkbookFactory.create(Objects.requireNonNull(getClass().getResourceAsStream("/TopicCodeMissing.xlsx")));
        Sheet syllabusSheet = workbook.getSheet("Syllabus");
        TechnicalGroup mockTechnicalGroup = new TechnicalGroup();
        mockTechnicalGroup.setId(1);
        mockTechnicalGroup.setCode("TG001");

        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.of(mockTechnicalGroup));
        when(topicRepository.findByTopicCodeAndVersion(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            excelImportService.importSyllabusSheet(syllabusSheet, false);
        });

        assertEquals("Topic Code is missing.", exception.getMessage());
    }

    @Test
    void importSyllabusSheet_shouldThrowExceptionWhenPassCriteriaMissing() throws Exception {
        // Arrange
        Workbook workbook = WorkbookFactory.create(Objects.requireNonNull(getClass().getResourceAsStream("/PassCriteriaMissing.xlsx")));
        Sheet syllabusSheet = workbook.getSheet("Syllabus");
        TechnicalGroup mockTechnicalGroup = new TechnicalGroup();
        mockTechnicalGroup.setId(1);
        mockTechnicalGroup.setCode("TG001");

        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.of(mockTechnicalGroup));
        when(topicRepository.findByTopicCodeAndVersion(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            excelImportService.importSyllabusSheet(syllabusSheet, false);
        });

        assertEquals("Pass Criteria is missing.", exception.getMessage());
    }

    @Test
    void importSyllabusSheet_shouldThrowExceptionWhenVersionMissing() throws Exception {
        // Arrange
        Workbook workbook = WorkbookFactory.create(Objects.requireNonNull(getClass().getResourceAsStream("/VersionMissing.xlsx")));
        Sheet syllabusSheet = workbook.getSheet("Syllabus");
        TechnicalGroup mockTechnicalGroup = new TechnicalGroup();
        mockTechnicalGroup.setId(1);
        mockTechnicalGroup.setCode("TG001");

        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.of(mockTechnicalGroup));
        when(topicRepository.findByTopicCodeAndVersion(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            excelImportService.importSyllabusSheet(syllabusSheet, false);
        });

        assertEquals("Version is missing.", exception.getMessage());
    }

    @Test
    void importSyllabusSheet_shouldThrowExceptionWhenTopicNameMissing() throws Exception {
        // Arrange
        Workbook workbook = WorkbookFactory.create(Objects.requireNonNull(getClass().getResourceAsStream("/TopicNameMissing.xlsx")));
        Sheet syllabusSheet = workbook.getSheet("Syllabus");
        TechnicalGroup mockTechnicalGroup = new TechnicalGroup();
        mockTechnicalGroup.setId(1);
        mockTechnicalGroup.setCode("TG001");

        when(technicalGroupRepository.findByCode(anyString())).thenReturn(Optional.of(mockTechnicalGroup));
        when(topicRepository.findByTopicCodeAndVersion(anyString(), anyString())).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            excelImportService.importSyllabusSheet(syllabusSheet, false);
        });

        assertEquals("Topic Name is missing.", exception.getMessage());
    }

    @Test
    void importDataFromStream_shouldThrowRuntimeExceptionWhenErrorOccurs() {
        // Arrange
        InputStream excelStream = getClass().getResourceAsStream("/InvalidExcelFile.xlsx");  // Simulate an invalid file

        // Mock the WorkbookFactory to throw an exception
        try (MockedStatic<WorkbookFactory> mockedWorkbookFactory = mockStatic(WorkbookFactory.class)) {
            mockedWorkbookFactory.when(() -> WorkbookFactory.create(excelStream))
                    .thenThrow(new IOException("Failed to create workbook"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                excelImportService.importDataFromStream(excelStream, false);
            });

            assertEquals("Failed to import Excel file: Failed to create workbook", exception.getMessage());
        }
    }

}
