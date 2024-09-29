package org.fms.training.mapper;

import org.fms.training.dto.deliverytype.DeliveryTypeDTO;
import org.fms.training.entity.DeliveryType;
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
