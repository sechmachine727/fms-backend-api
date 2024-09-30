package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.locationdto.LocationDTO;
import org.fms.training.entity.Location;
import org.fms.training.mapper.LocationMapper;
import org.fms.training.repository.LocationRepository;
import org.fms.training.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public List<LocationDTO> getLocationsBySiteId(Integer siteId) {
        List<Location> locations = locationRepository.findBySiteId(siteId);
        return locationMapper.toLocationDTOs(locations);
    }
}
