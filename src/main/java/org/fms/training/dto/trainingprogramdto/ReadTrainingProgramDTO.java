package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.Status;

@Data
@NoArgsConstructor
public class ReadTrainingProgramDTO {
    private String name;
    private String code;
    private String region;
    private Integer version;
    private String description;
    private Status status;
    private String technicalGroupCode;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
//    private List<Topic> topics;
}
