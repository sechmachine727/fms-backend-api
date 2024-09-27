package org.fms.training.service.impl;

import lombok.RequiredArgsConstructor;
import org.fms.training.entity.FormatType;
import org.fms.training.repository.FormatTypeRepository;
import org.fms.training.service.FormatTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormatTypeServiceImpl implements FormatTypeService {

    private final FormatTypeRepository formatTypeRepository;

    @Override
    public Optional<List<FormatType>> getAllFormatTypes() {
        List<FormatType> formatTypes = formatTypeRepository.findAll();
        return formatTypes.isEmpty() ? Optional.empty() : Optional.of(formatTypes);
    }
}
