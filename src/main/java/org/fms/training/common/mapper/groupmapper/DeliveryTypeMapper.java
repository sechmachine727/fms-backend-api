package org.fms.training.common.mapper.groupmapper;

import org.fms.training.common.dto.deliverytypedto.DeliveryTypeDTO;
import org.fms.training.common.entity.DeliveryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryTypeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "deliveryTypeName", target = "deliveryTypeName")
    DeliveryTypeDTO toDeliveryTypeDTO(DeliveryType deliveryType);

    List<DeliveryTypeDTO> toDeliveryTypeDTOs(List<DeliveryType> deliveryTypes);
}
