package org.fms.training.service.impl;

import org.fms.training.mapper.DeliveryTypeMapper;
import org.fms.training.repository.DeliveryTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.List;

class DeliveryTypeServiceImplTest {

    @Mock
    private DeliveryTypeRepository deliveryTypeRepository;

    @Mock
    private DeliveryTypeMapper deliveryTypeMapper;

    @InjectMocks
    private DeliveryTypeServiceImpl deliveryTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDeliveryTypes_shouldReturnListOfDeliveryTypes() {
        // when
        deliveryTypeService.getAllDeliveryTypes();

        // then
        then(deliveryTypeRepository).should(times(1)).findAll();
        then(deliveryTypeMapper).should(times(1)).toDeliveryTypeDTOs(List.of());
    }
}
