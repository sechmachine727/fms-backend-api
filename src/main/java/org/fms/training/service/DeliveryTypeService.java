package org.fms.training.service;

import org.fms.training.common.dto.deliverytypedto.DeliveryTypeDTO;

import java.util.List;

public interface DeliveryTypeService {
    List<DeliveryTypeDTO> getAllDeliveryTypes();
}
