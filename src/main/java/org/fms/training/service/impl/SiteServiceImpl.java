package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.sitedto.SiteDTO;
import org.fms.training.common.mapper.groupmapper.SiteMapper;
import org.fms.training.repository.SiteRepository;
import org.fms.training.service.SiteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;

    @Override
    public List<SiteDTO> getAllSites() {
        return siteMapper.toSiteDTOs(siteRepository.findAll());
    }
}
