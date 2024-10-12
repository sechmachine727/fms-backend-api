package org.fms.training.service.impl;

import org.fms.training.dto.sitedto.SiteDTO;
import org.fms.training.entity.Site;
import org.fms.training.mapper.SiteMapper;
import org.fms.training.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class SiteServiceImplTest {

    @Mock
    private SiteRepository siteRepository;

    @Mock
    private SiteMapper siteMapper;

    @InjectMocks
    private SiteServiceImpl siteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSites_shouldReturnMappedSiteDTOs_whenSitesExist() {
        // given
        Site site1 = new Site();
        Site site2 = new Site();
        List<Site> sites = List.of(site1, site2);

        SiteDTO siteDTO1 = new SiteDTO();
        SiteDTO siteDTO2 = new SiteDTO();
        List<SiteDTO> siteDTOs = List.of(siteDTO1, siteDTO2);

        given(siteRepository.findAll()).willReturn(sites);
        given(siteMapper.toSiteDTOs(sites)).willReturn(siteDTOs);

        // when
        List<SiteDTO> result = siteService.getAllSites();

        // then
        assertThat(result).hasSize(2);
        verify(siteRepository, times(1)).findAll();
        verify(siteMapper, times(1)).toSiteDTOs(sites);
    }

    @Test
    void getAllSites_shouldReturnEmptyList_whenNoSitesExist() {
        // given
        given(siteRepository.findAll()).willReturn(List.of());
        given(siteMapper.toSiteDTOs(List.of())).willReturn(List.of());

        // when
        List<SiteDTO> result = siteService.getAllSites();

        // then
        assertThat(result).isEmpty();
        verify(siteRepository, times(1)).findAll();
        verify(siteMapper, times(1)).toSiteDTOs(List.of());
    }
}
