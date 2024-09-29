package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.locationdto.LocationDTO;
import org.fms.training.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<LocationDTO>> getLocationsBySiteId(@PathVariable Integer siteId) {
        List<LocationDTO> locations = locationService.getLocationsBySiteId(siteId);
        return ResponseEntity.ok(locations);
    }
}
