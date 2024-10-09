package org.fms.training.service.impl;

import org.fms.training.dto.trainingprogramdto.SaveTrainingProgramDTO;
import org.fms.training.entity.Topic;
import org.fms.training.entity.TrainingProgram;
import org.fms.training.enums.Status;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.mapper.TrainingProgramMapper;
import org.fms.training.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

class TrainingProgramServiceImplTest {

    @Mock
    private TrainingProgramRepository trainingProgramRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicTrainingProgramRepository topicTrainingProgramRepository;

    @Mock
    private TechnicalGroupRepository technicalGroupRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TrainingProgramMapper trainingProgramMapper;

    @InjectMocks
    private TrainingProgramServiceImpl trainingProgramService;

    @BeforeEach
    void setUp() throws Exception {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            // Initialization code if needed
        }
    }

    @Test
    void getTrainingProgramById_withExistentTrainingProgram_shouldReturnTrainingProgram() {
        // given
        Integer trainingProgramId = 1;
        TrainingProgram trainingProgram = new TrainingProgram();
        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.of(trainingProgram));

        // when
        trainingProgramService.getTrainingProgramById(trainingProgramId);

        // then
        then(trainingProgramRepository).should(times(1)).findById(trainingProgramId);
    }

    @Test
    void getAllTrainingPrograms_shouldReturnListOfTrainingPrograms() {
        // when
        trainingProgramService.getAllTrainingPrograms(null);

        // then
        then(trainingProgramRepository).should(times(1)).findByTrainingProgramNameContainingIgnoreCaseOrCodeContainingIgnoreCase(null);
    }

    @Test
    void findByTechnicalGroupId_withExistentTechnicalGroup_shouldReturnListOfTrainingPrograms() {
        // given
        Integer technicalGroupId = 1;
        given(trainingProgramRepository.findByTechnicalGroupId(technicalGroupId)).willReturn(Collections.emptyList());

        // when
        trainingProgramService.findByTechnicalGroupId(technicalGroupId);

        // then
        then(trainingProgramRepository).should(times(1)).findByTechnicalGroupId(technicalGroupId);
    }

    @Test
    void createTrainingProgram_withValidData_shouldSaveTrainingProgram() {
        // given
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();
        saveTrainingProgramDTO.setCode("TP001");
        saveTrainingProgramDTO.setTopicIds(List.of(1, 2, 3));
        saveTrainingProgramDTO.setTechnicalGroupId(1);
        saveTrainingProgramDTO.setDepartmentId(1);

        TrainingProgram trainingProgram = new TrainingProgram();
        given(trainingProgramMapper.toTrainingProgramEntity(saveTrainingProgramDTO)).willReturn(trainingProgram);
        given(trainingProgramRepository.save(trainingProgram)).willReturn(trainingProgram);

        Topic topic = new Topic();
        topic.setStatus(Status.ACTIVE);
        given(topicRepository.findById(anyInt())).willReturn(Optional.of(topic));
        given(technicalGroupRepository.existsById(saveTrainingProgramDTO.getTechnicalGroupId())).willReturn(true);
        given(departmentRepository.existsById(saveTrainingProgramDTO.getDepartmentId())).willReturn(true);

        // when
        trainingProgramService.createTrainingProgram(saveTrainingProgramDTO);

        // then
        then(trainingProgramRepository).should(times(1)).save(trainingProgram);
        then(topicTrainingProgramRepository).should(times(1)).saveAll(anyList());
    }

    @Test
    void createTrainingProgram_withDuplicateCode_shouldThrowValidationException() {
        // given
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();
        saveTrainingProgramDTO.setCode("TP001");

        given(trainingProgramRepository.existsByCode(saveTrainingProgramDTO.getCode())).willReturn(true);

        // when, then
        assertThrows(ValidationException.class, () -> trainingProgramService.createTrainingProgram(saveTrainingProgramDTO));
    }

    @Test
    void createTrainingProgram_withNonExistentTechnicalGroup_shouldThrowValidationException() {
        // given
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();
        saveTrainingProgramDTO.setTechnicalGroupId(1);

        given(technicalGroupRepository.existsById(saveTrainingProgramDTO.getTechnicalGroupId())).willReturn(false);

        // when, then
        assertThrows(ValidationException.class, () -> trainingProgramService.createTrainingProgram(saveTrainingProgramDTO));
    }

    @Test
    void createTrainingProgram_withNonExistentDepartment_shouldThrowValidationException() {
        // given
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();
        saveTrainingProgramDTO.setDepartmentId(1);

        given(departmentRepository.existsById(saveTrainingProgramDTO.getDepartmentId())).willReturn(false);

        // when, then
        assertThrows(ValidationException.class, () -> trainingProgramService.createTrainingProgram(saveTrainingProgramDTO));
    }

    @Test
    void updateTrainingProgram_withValidData_shouldUpdateTrainingProgram() {
        // given
        Integer trainingProgramId = 1;
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();
        saveTrainingProgramDTO.setCode("TP001");
        saveTrainingProgramDTO.setTopicIds(List.of(1, 2, 3));
        saveTrainingProgramDTO.setTechnicalGroupId(1);
        saveTrainingProgramDTO.setDepartmentId(1);

        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setCode("TP001");

        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.of(trainingProgram));
        willDoNothing().given(topicTrainingProgramRepository).deleteAll(anyList());
        given(trainingProgramRepository.save(trainingProgram)).willReturn(trainingProgram);

        Topic topic = new Topic();
        topic.setStatus(Status.ACTIVE);
        given(topicRepository.findById(anyInt())).willReturn(Optional.of(topic));
        given(technicalGroupRepository.existsById(saveTrainingProgramDTO.getTechnicalGroupId())).willReturn(true);
        given(departmentRepository.existsById(saveTrainingProgramDTO.getDepartmentId())).willReturn(true);

        // when
        trainingProgramService.updateTrainingProgram(trainingProgramId, saveTrainingProgramDTO);

        // then
        then(trainingProgramRepository).should(times(1)).save(trainingProgram);
        then(topicTrainingProgramRepository).should(times(1)).saveAll(anyList());
    }

    @Test
    void updateTrainingProgram_withNonExistentTrainingProgram_shouldThrowResourceNotFoundException() {
        // given
        Integer trainingProgramId = 1;
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();

        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.empty());

        // when, then
        assertThrows(ResourceNotFoundException.class, () -> trainingProgramService.updateTrainingProgram(trainingProgramId, saveTrainingProgramDTO));
    }

    @Test
    void updateTrainingProgram_withDuplicateCode_shouldThrowValidationException() {
        // given
        Integer trainingProgramId = 1;
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();
        saveTrainingProgramDTO.setCode("TP001");

        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setCode("TP002");

        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.of(trainingProgram));
        given(trainingProgramRepository.existsByCode(saveTrainingProgramDTO.getCode())).willReturn(true);

        // when, then
        assertThrows(ValidationException.class, () -> trainingProgramService.updateTrainingProgram(trainingProgramId, saveTrainingProgramDTO));
    }

    @Test
    void updateTrainingProgram_withNonExistentTechnicalGroup_shouldThrowValidationException() {
        // given
        Integer trainingProgramId = 1;
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();
        saveTrainingProgramDTO.setTechnicalGroupId(1);

        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setCode("TP001");

        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.of(trainingProgram));
        given(technicalGroupRepository.existsById(saveTrainingProgramDTO.getTechnicalGroupId())).willReturn(false);

        // when, then
        assertThrows(ValidationException.class, () -> trainingProgramService.updateTrainingProgram(trainingProgramId, saveTrainingProgramDTO));
    }

    @Test
    void updateTrainingProgram_withNonExistentDepartment_shouldThrowValidationException() {
        // given
        Integer trainingProgramId = 1;
        SaveTrainingProgramDTO saveTrainingProgramDTO = new SaveTrainingProgramDTO();
        saveTrainingProgramDTO.setDepartmentId(1);

        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setCode("TP001");

        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.of(trainingProgram));
        given(departmentRepository.existsById(saveTrainingProgramDTO.getDepartmentId())).willReturn(false);

        // when, then
        assertThrows(ValidationException.class, () -> trainingProgramService.updateTrainingProgram(trainingProgramId, saveTrainingProgramDTO));
    }

    @Test
    void updateTrainingProgramStatus_withActiveStatus_shouldSetToInactive() {
        // given
        Integer trainingProgramId = 1;
        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setStatus(Status.ACTIVE);
        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.of(trainingProgram));

        // when
        trainingProgramService.updateTrainingProgramStatus(trainingProgramId, Status.INACTIVE);

        // then
        then(trainingProgramRepository).should(times(1)).save(trainingProgram);
        assert trainingProgram.getStatus() == Status.INACTIVE;
    }

    @Test
    void updateTrainingProgramStatus_withInactiveStatus_shouldSetToActive() {
        // given
        Integer trainingProgramId = 1;
        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setStatus(Status.INACTIVE);
        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.of(trainingProgram));

        // when
        trainingProgramService.updateTrainingProgramStatus(trainingProgramId, Status.ACTIVE);

        // then
        then(trainingProgramRepository).should(times(1)).save(trainingProgram);
        assert trainingProgram.getStatus() == Status.ACTIVE;
    }

    @Test
    void updateTrainingProgramStatus_withNonExistentTrainingProgram_shouldThrowResourceNotFoundException() {
        // given
        Integer trainingProgramId = 1;
        given(trainingProgramRepository.findById(trainingProgramId)).willReturn(Optional.empty());

        // when, then
        assertThrows(ResourceNotFoundException.class, () -> trainingProgramService.updateTrainingProgramStatus(trainingProgramId, Status.ACTIVE));
    }
}