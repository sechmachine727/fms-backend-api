package org.fms.training.service;

import org.fms.training.dto.deliverytype.DeliveryTypeDTO;

import java.util.List;

public interface DeliveryTypeService {
    List<DeliveryTypeDTO> getAllDeliveryTypes();
}
