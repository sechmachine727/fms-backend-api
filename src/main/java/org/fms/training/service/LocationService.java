package org.fms.training.service;

import org.fms.training.common.dto.locationdto.LocationDTO;

import java.util.List;

public interface LocationService {
    List<LocationDTO> getLocationsBySiteId(Integer siteId);
}
