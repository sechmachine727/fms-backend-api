package org.fms.training.common.dto.unitsectiondto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnitSectionDTO {
    private String title;
    private String deliveryType;
    private Double duration;
    private String trainingFormat;
    private String note;
}
