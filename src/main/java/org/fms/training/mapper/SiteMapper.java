package org.fms.training.mapper;

import org.fms.training.dto.sitedto.SiteDTO;
import org.fms.training.entity.Site;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SiteMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "siteName", target = "siteName")
    SiteDTO toSiteDTO(Site site);

    List<SiteDTO> toSiteDTOs(List<Site> sites);
}
