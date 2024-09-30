package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.dto.deliverytypedto.DeliveryTypeDTO;
import org.fms.training.mapper.DeliveryTypeMapper;
import org.fms.training.repository.DeliveryTypeRepository;
import org.fms.training.service.DeliveryTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryTypeServiceImpl implements DeliveryTypeService {

    private final DeliveryTypeRepository deliveryTypeRepository;
    private final DeliveryTypeMapper deliveryTypeMapper;

    @Override
    public List<DeliveryTypeDTO> getAllDeliveryTypes() {
        return deliveryTypeMapper.toDeliveryTypeDTOs(deliveryTypeRepository.findAll());
    }
}
