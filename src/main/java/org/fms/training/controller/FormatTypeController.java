package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.FormatType;
import org.fms.training.service.FormatTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/format-types")
@RequiredArgsConstructor
public class FormatTypeController {

    private final FormatTypeService formatTypeService;

    @GetMapping
    public ResponseEntity<List<FormatType>> getAllFormatTypes() {
        Optional<List<FormatType>> formatTypesOpt = formatTypeService.getAllFormatTypes();
        return formatTypesOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
