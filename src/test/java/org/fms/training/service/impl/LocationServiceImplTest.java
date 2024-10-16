package org.fms.training.service.impl;

import org.fms.training.common.dto.locationdto.LocationDTO;
import org.fms.training.common.entity.Location;
import org.fms.training.common.mapper.LocationMapper;
import org.fms.training.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationServiceImpl locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLocationsBySiteId_shouldReturnLocationDTOs_whenLocationsExist() {
        // given
        Integer siteId = 1;
        Location location1 = new Location();
        Location location2 = new Location();
        List<Location> locations = List.of(location1, location2);
        LocationDTO locationDTO1 = new LocationDTO();
        LocationDTO locationDTO2 = new LocationDTO();
        List<LocationDTO> locationDTOs = List.of(locationDTO1, locationDTO2);

        given(locationRepository.findBySiteId(siteId)).willReturn(locations);
        given(locationMapper.toLocationDTOs(locations)).willReturn(locationDTOs);

        // when
        List<LocationDTO> result = locationService.getLocationsBySiteId(siteId);

        // then
        assertThat(result).hasSize(2);
        verify(locationRepository, times(1)).findBySiteId(siteId);
        verify(locationMapper, times(1)).toLocationDTOs(locations);
    }

    @Test
    void getLocationsBySiteId_shouldReturnEmptyList_whenNoLocationsExist() {
        // given
        Integer siteId = 1;
        given(locationRepository.findBySiteId(siteId)).willReturn(List.of());

        // when
        List<LocationDTO> result = locationService.getLocationsBySiteId(siteId);

        // then
        assertThat(result).isEmpty();
        verify(locationRepository, times(1)).findBySiteId(siteId);
        verify(locationMapper, times(1)).toLocationDTOs(List.of());
    }
}
