package org.fms.training.service.impl;

// Import necessary classes and annotations

import org.fms.training.dto.traineedto.ListTraineeDTO;
import org.fms.training.dto.traineedto.ReadTraineeDTO;
import org.fms.training.dto.traineedto.SaveTraineeDTO;
import org.fms.training.entity.Trainee;
import org.fms.training.mapper.TraineeMapper;
import org.fms.training.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

//Name the class after the implementation class being tested, suffixed with Test
class TraineeServiceImplTest {

    // Mock dependencies
    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TraineeMapper traineeMapper;

    // Inject mocks into the service being tested
    @InjectMocks
    private TraineeServiceImpl traineeService;

    // Initialize mocks before each test
    @BeforeEach
    void setUp() throws Exception {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            // Initialization code if needed
        }
    }

    // Test for getAllTrainees method
    @Test
    void getAllTrainees() {
        // Given: Setup mock behavior and test data
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        ListTraineeDTO listTraineeDTO1 = new ListTraineeDTO();
        ListTraineeDTO listTraineeDTO2 = new ListTraineeDTO();
        List<Trainee> trainees = List.of(trainee1, trainee2);

        given(traineeRepository.findAll()).willReturn(trainees);
        given(traineeMapper.toListTraineeDTO(trainee1)).willReturn(listTraineeDTO1);
        given(traineeMapper.toListTraineeDTO(trainee2)).willReturn(listTraineeDTO2);

        // When: Call the method under test
        Optional<List<ListTraineeDTO>> result = traineeService.getAllTrainees();

        // Then: Verify the results and interactions
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
        then(traineeRepository).should().findAll();
        then(traineeMapper).should(times(2)).toListTraineeDTO(any(Trainee.class));
    }

    // Test for getTraineeById method when trainee is found
    @Test
    void getTraineeById() {
        // Given: Setup mock behavior and test data
        Integer id = 1;
        Trainee trainee = new Trainee();
        ReadTraineeDTO readTraineeDTO = new ReadTraineeDTO();

        given(traineeRepository.findById(id)).willReturn(Optional.of(trainee));
        given(traineeMapper.toReadTraineeDTO(trainee)).willReturn(readTraineeDTO);

        // When: Call the method under test
        Optional<ReadTraineeDTO> result = traineeService.getTraineeById(id);

        // Then: Verify the results and interactions
        assertThat(result).isPresent().contains(readTraineeDTO);
        then(traineeRepository).should().findById(id);
        then(traineeMapper).should().toReadTraineeDTO(trainee);
    }

    // Test for getTraineeById method when trainee is not found
    @Test
    void getTraineeById_NotFound() {
        // Given: Setup mock behavior
        Integer id = 1;

        given(traineeRepository.findById(id)).willReturn(Optional.empty());

        // When: Call the method under test
        Optional<ReadTraineeDTO> result = traineeService.getTraineeById(id);

        // Then: Verify the results and interactions
        assertThat(result).isNotPresent();
        then(traineeRepository).should().findById(id);
        then(traineeMapper).shouldHaveNoInteractions();
    }

    // Test for addTrainee method
    @Test
    void addTrainee() {
        // Given: Setup mock behavior and test data
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        Trainee trainee = new Trainee();

        given(traineeMapper.toTraineeEntity(saveTraineeDTO)).willReturn(trainee);

        // When: Call the method under test
        traineeService.addTrainee(saveTraineeDTO);

        // Then: Verify the interactions
        then(traineeMapper).should().toTraineeEntity(saveTraineeDTO);
        then(traineeRepository).should().save(trainee);
    }

    // Test for updateTrainee method when trainee is found
    @Test
    void updateTrainee() {
        // Given: Setup mock behavior and test data
        Integer id = 1;
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();
        Trainee trainee = new Trainee();

        given(traineeRepository.findById(id)).willReturn(Optional.of(trainee));

        // When: Call the method under test
        traineeService.updateTrainee(id, saveTraineeDTO);

        // Then: Verify the interactions
        then(traineeRepository).should().findById(id);
        then(traineeMapper).should().updateTraineeFromDTO(saveTraineeDTO, trainee);
    }

    // Test for updateTrainee method when trainee is not found
    @Test
    void updateTrainee_NotFound() {
        // Given: Setup mock behavior
        Integer id = 1;
        SaveTraineeDTO saveTraineeDTO = new SaveTraineeDTO();

        given(traineeRepository.findById(id)).willReturn(Optional.empty());

        // When / Then: Call the method under test and verify the exception
        assertThrows(RuntimeException.class, () -> traineeService.updateTrainee(id, saveTraineeDTO));
        then(traineeRepository).should().findById(id);
        then(traineeMapper).shouldHaveNoInteractions();
    }
}