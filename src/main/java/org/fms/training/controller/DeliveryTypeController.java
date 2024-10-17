package org.fms.training.controller;

import lombok.RequiredArgsConstructor;
import org.fms.training.common.dto.deliverytypedto.DeliveryTypeDTO;
import org.fms.training.service.DeliveryTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-types")
@RequiredArgsConstructor
public class DeliveryTypeController {

    private final DeliveryTypeService deliveryTypeService;

    @GetMapping
    public ResponseEntity<List<DeliveryTypeDTO>> getAllDeliveryTypes() {
        List<DeliveryTypeDTO> deliveryTypes = deliveryTypeService.getAllDeliveryTypes();
        return ResponseEntity.ok(deliveryTypes);
    }
}
