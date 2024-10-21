package org.fms.training.common.dto.trainingcalendardto.external;

import org.fms.training.common.dto.unitsectiondto.UnitSectionDTO;

import java.util.List;

public record SimpleUnitDTO(
        String unitName,
        List<UnitSectionDTO> unitSections
) {
}
