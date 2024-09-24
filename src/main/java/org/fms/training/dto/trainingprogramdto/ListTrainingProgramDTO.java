package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListTrainingProgramDTO {
    private String name;
    private String code;
    private Integer version;
    private String description;
    private String status;
    private String technicalGroupCode;
    private String lastModifiedBy;
    private String modifiedDate;
}
