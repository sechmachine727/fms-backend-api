package org.fms.training.service.impl;

import org.fms.training.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.entity.TechnicalGroup;
import org.fms.training.mapper.TechnicalGroupMapper;
import org.fms.training.repository.TechnicalGroupRepository;
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

class TechnicalGroupServiceImplTest {

    @Mock
    private TechnicalGroupRepository technicalGroupRepository;

    @Mock
    private TechnicalGroupMapper technicalGroupMapper;

    @InjectMocks
    private TechnicalGroupServiceImpl technicalGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTechnicalGroups_shouldReturnMappedTechnicalGroupDTOs_whenTechnicalGroupsExist() {
        // given
        TechnicalGroup group1 = new TechnicalGroup();
        TechnicalGroup group2 = new TechnicalGroup();
        List<TechnicalGroup> technicalGroups = List.of(group1, group2);

        ListTechnicalGroupDTO dto1 = new ListTechnicalGroupDTO();
        ListTechnicalGroupDTO dto2 = new ListTechnicalGroupDTO();
        List<ListTechnicalGroupDTO> listTechnicalGroupDTOs = List.of(dto1, dto2);

        given(technicalGroupRepository.findAll()).willReturn(technicalGroups);
        given(technicalGroupMapper.toListDTO(group1)).willReturn(dto1);
        given(technicalGroupMapper.toListDTO(group2)).willReturn(dto2);

        // when
        Optional<List<ListTechnicalGroupDTO>> result = technicalGroupService.getAllTechnicalGroups();

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
        verify(technicalGroupRepository, times(1)).findAll();
        verify(technicalGroupMapper, times(1)).toListDTO(group1);
        verify(technicalGroupMapper, times(1)).toListDTO(group2);
    }

    @Test
    void getAllTechnicalGroups_shouldReturnEmptyOptional_whenNoTechnicalGroupsExist() {
        // given
        given(technicalGroupRepository.findAll()).willReturn(List.of());

        // when
        Optional<List<ListTechnicalGroupDTO>> result = technicalGroupService.getAllTechnicalGroups();

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty();
        verify(technicalGroupRepository, times(1)).findAll();
    }
}
