package org.fms.training.service;

import org.fms.training.entity.FormatType;

import java.util.List;
import java.util.Optional;

public interface FormatTypeService {
    Optional<List<FormatType>> getAllFormatTypes();
}
