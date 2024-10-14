package org.fms.training.service.impl;

import org.fms.training.entity.FormatType;
import org.fms.training.repository.FormatTypeRepository;
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

class FormatTypeServiceImplTest {

    @Mock
    private FormatTypeRepository formatTypeRepository;

    @InjectMocks
    private FormatTypeServiceImpl formatTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllFormatTypes_shouldReturnListOfFormatTypes_whenFormatTypesExist() {
        // given
        FormatType formatType1 = new FormatType();
        FormatType formatType2 = new FormatType();
        given(formatTypeRepository.findAll()).willReturn(List.of(formatType1, formatType2));

        // when
        Optional<List<FormatType>> result = formatTypeService.getAllFormatTypes();

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
        verify(formatTypeRepository, times(1)).findAll();
    }

    @Test
    void getAllFormatTypes_shouldReturnEmptyOptional_whenNoFormatTypesExist() {
        // given
        given(formatTypeRepository.findAll()).willReturn(List.of());

        // when
        Optional<List<FormatType>> result = formatTypeService.getAllFormatTypes();

        // then
        assertThat(result).isEmpty();
        verify(formatTypeRepository, times(1)).findAll();
    }
}
