package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.sitedto.SiteDTO;
import org.fms.training.service.SiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @GetMapping
    public ResponseEntity<List<SiteDTO>> getAllSites() {
        List<SiteDTO> sites = siteService.getAllSites();
        return ResponseEntity.ok(sites);
    }
}
