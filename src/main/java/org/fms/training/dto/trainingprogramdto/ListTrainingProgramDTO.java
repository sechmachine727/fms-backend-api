package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.Status;

@Data
@NoArgsConstructor
public class ListTrainingProgramDTO {
    private String name;
    private String code;
    private Integer version;
    private String description;
    private Status status;
    private String technicalGroupCode;
    private String lastModifiedBy;
    private String modifiedDate;
}
