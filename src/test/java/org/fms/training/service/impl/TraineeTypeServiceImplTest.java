package org.fms.training.service.impl;

import org.fms.training.common.entity.TraineeType;
import org.fms.training.repository.TraineeTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TraineeTypeServiceImplTest {

    @Mock
    private TraineeTypeRepository traineeTypeRepository;

    @InjectMocks
    private TraineeTypeServiceImpl traineeTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTraineeTypes_shouldReturnTraineeTypes_whenTheyExist() {
        // given
        TraineeType traineeType1 = new TraineeType();
        TraineeType traineeType2 = new TraineeType();
        List<TraineeType> traineeTypes = List.of(traineeType1, traineeType2);

        given(traineeTypeRepository.findAll()).willReturn(traineeTypes);

        // when
        Optional<List<TraineeType>> result = traineeTypeService.getAllTraineeTypes();

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
        verify(traineeTypeRepository, times(1)).findAll();
    }

    @Test
    void getAllTraineeTypes_shouldReturnEmptyOptional_whenNoTraineeTypesExist() {
        // given
        given(traineeTypeRepository.findAll()).willReturn(List.of());

        // when
        Optional<List<TraineeType>> result = traineeTypeService.getAllTraineeTypes();

        // then
        assertThat(result).isEmpty();
        verify(traineeTypeRepository, times(1)).findAll();
    }
}
