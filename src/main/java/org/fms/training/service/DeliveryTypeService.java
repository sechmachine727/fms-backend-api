package org.fms.training.service;

import org.fms.training.dto.deliverytypedto.DeliveryTypeDTO;

import java.util.List;

public interface DeliveryTypeService {
    List<DeliveryTypeDTO> getAllDeliveryTypes();
}
