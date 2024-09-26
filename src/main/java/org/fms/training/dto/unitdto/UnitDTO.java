package org.fms.training.dto.unitdto;

import org.fms.training.dto.unitsectiondto.UnitSectionDTO;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

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
