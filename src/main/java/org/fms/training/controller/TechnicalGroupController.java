package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.service.TechnicalGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/technical-groups")
@RestController
public class TechnicalGroupController {
    private final TechnicalGroupService technicalGroupService;

    @GetMapping
    public ResponseEntity<List<ListTechnicalGroupDTO>> findAll() {
        Optional<List<ListTechnicalGroupDTO>> result = technicalGroupService.getAllTechnicalGroups();
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
