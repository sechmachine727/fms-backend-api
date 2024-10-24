package org.fms.training.common.mapper.groupmapper;

import org.fms.training.common.dto.locationdto.LocationDTO;
import org.fms.training.common.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "locationName", target = "locationName")
    LocationDTO toLocationDTO(Location location);

    List<LocationDTO> toLocationDTOs(List<Location> locations);
}
