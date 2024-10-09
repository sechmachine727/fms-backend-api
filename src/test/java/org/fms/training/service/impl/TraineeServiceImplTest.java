package org.fms.training.service.impl;

import org.fms.training.dto.traineedto.SaveTraineeDTO;
import org.fms.training.entity.Trainee;
import org.fms.training.exception.ResourceNotFoundException;
import org.fms.training.exception.ValidationException;
import org.fms.training.mapper.TraineeMapper;
import org.fms.training.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TraineeMapper traineeMapper;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() throws Exception {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            // Initialization code if needed
        }
    }

    @Test
    void addTrainee_withValidData_shouldSaveTrainee() {
        // given
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        saveTraineeDTO.setEmail("test@example.com");
        saveTraineeDTO.setPhone("1234567890");
        saveTraineeDTO.setNationalId("ID123");

        Trainee trainee = new Trainee();
        given(traineeMapper.toTraineeEntity(saveTraineeDTO)).willReturn(trainee);
        given(traineeRepository.save(trainee)).willReturn(trainee);

        // when
        traineeService.addTrainee(saveTraineeDTO);

        // then
        then(traineeRepository).should(times(1)).save(trainee);
    }

    @Test
    void addTrainee_withDuplicateEmail_shouldThrowValidationException() {
        // given
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        saveTraineeDTO.setEmail("duplicate@example.com");

        given(traineeRepository.existsByEmail(saveTraineeDTO.getEmail())).willReturn(true);

        // when, then
        assertThrows(ValidationException.class, () -> traineeService.addTrainee(saveTraineeDTO));
    }

    @Test
    void addTrainee_withDuplicatePhone_shouldThrowValidationException() {
        // given
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        saveTraineeDTO.setPhone("1234567890");

        given(traineeRepository.existsByPhone(saveTraineeDTO.getPhone())).willReturn(true);

        // when, then
        assertThrows(ValidationException.class, () -> traineeService.addTrainee(saveTraineeDTO));
    }

    @Test
    void addTrainee_withDuplicateNationalId_shouldThrowValidationException() {
        // given
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        saveTraineeDTO.setNationalId("ID123");

        given(traineeRepository.existsByNationalId(saveTraineeDTO.getNationalId())).willReturn(true);

        // when, then
        assertThrows(ValidationException.class, () -> traineeService.addTrainee(saveTraineeDTO));
    }

    @Test
    void updateTrainee_withValidData_shouldUpdateTrainee() {
        // given
        Integer traineeId = 1;
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        saveTraineeDTO.setEmail("test@example.com");
        saveTraineeDTO.setPhone("1234567890");
        saveTraineeDTO.setNationalId("ID123");

        Trainee trainee = new Trainee();
        trainee.setEmail("old@example.com");
        trainee.setPhone("0987654321");
        trainee.setNationalId("ID456");

        given(traineeRepository.findById(traineeId)).willReturn(Optional.of(trainee));
        willDoNothing().given(traineeMapper).updateTraineeFromDTO(saveTraineeDTO, trainee);

        // when
        traineeService.updateTrainee(traineeId, saveTraineeDTO);

        // then
        then(traineeMapper).should(times(1)).updateTraineeFromDTO(saveTraineeDTO, trainee);
    }

    @Test
    void updateTrainee_withNonExistentTrainee_shouldThrowResourceNotFoundException() {
        // given
        Integer traineeId = 1;
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();

        given(traineeRepository.findById(traineeId)).willReturn(Optional.empty());

        // when, then
        assertThrows(ResourceNotFoundException.class, () -> traineeService.updateTrainee(traineeId, saveTraineeDTO));
    }

    @Test
    void updateTrainee_withDuplicateEmail_shouldThrowValidationException() {
        // given
        Integer traineeId = 1;
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        saveTraineeDTO.setEmail("duplicate@example.com");
        saveTraineeDTO.setPhone("1234567890");
        saveTraineeDTO.setNationalId("ID123");

        Trainee trainee = new Trainee();
        trainee.setEmail("old@example.com");
        trainee.setPhone("1234567890");
        trainee.setNationalId("ID123");

        given(traineeRepository.findById(traineeId)).willReturn(Optional.of(trainee));
        given(traineeRepository.existsByEmail(saveTraineeDTO.getEmail())).willReturn(true);
        given(traineeRepository.existsByPhone(saveTraineeDTO.getPhone())).willReturn(false);
        given(traineeRepository.existsByNationalId(saveTraineeDTO.getNationalId())).willReturn(false);

        assertThrows(ValidationException.class, () -> traineeService.updateTrainee(traineeId, saveTraineeDTO));
    }

    @Test
    void updateTrainee_withDuplicatePhone_shouldThrowValidationException() {
        // given
        Integer traineeId = 1;
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        saveTraineeDTO.setEmail("test@example.com");
        saveTraineeDTO.setPhone("1234567890");
        saveTraineeDTO.setNationalId("ID123");

        Trainee trainee = new Trainee();
        trainee.setEmail("test@example.com");
        trainee.setPhone("0987654321");
        trainee.setNationalId("ID123");

        given(traineeRepository.findById(traineeId)).willReturn(Optional.of(trainee));
        given(traineeRepository.existsByPhone(saveTraineeDTO.getPhone())).willReturn(true);
        given(traineeRepository.existsByEmail(saveTraineeDTO.getEmail())).willReturn(false);
        given(traineeRepository.existsByNationalId(saveTraineeDTO.getNationalId())).willReturn(false);

        // when, then
        assertThrows(ValidationException.class, () -> traineeService.updateTrainee(traineeId, saveTraineeDTO));
    }

    @Test
    void updateTrainee_withDuplicateNationalId_shouldThrowValidationException() {
        // given
        Integer traineeId = 1;
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        saveTraineeDTO.setEmail("test@example.com");
        saveTraineeDTO.setPhone("1234567890");
        saveTraineeDTO.setNationalId("ID123");

        Trainee trainee = new Trainee();
        trainee.setEmail("test@example.com");
        trainee.setPhone("1234567890");
        trainee.setNationalId("ID456");

        given(traineeRepository.findById(traineeId)).willReturn(Optional.of(trainee));
        given(traineeRepository.existsByPhone(saveTraineeDTO.getPhone())).willReturn(false);
        given(traineeRepository.existsByEmail(saveTraineeDTO.getEmail())).willReturn(false);
        given(traineeRepository.existsByNationalId(saveTraineeDTO.getNationalId())).willReturn(true);

        // when, then
        assertThrows(ValidationException.class, () -> traineeService.updateTrainee(traineeId, saveTraineeDTO));
    }
}