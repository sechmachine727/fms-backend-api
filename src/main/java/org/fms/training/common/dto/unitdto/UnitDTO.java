package org.fms.training.common.dto.unitdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.dto.unitsectiondto.UnitSectionDTO;

import java.util.List;

@Data
@NoArgsConstructor
public class UnitDTO {
    private String unitName;
    private List<UnitSectionDTO> unitSections;
    private Double totalDurationClassMeeting;
    private Double totalDurationGuidesReview;
    private Double totalDurationProductIncrement;
    private Double totalDuration;
}
