package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadTrainingProgramDTO {
    private String name;
    private String code;
    private String region;
    private Integer version;
    private String description;
    private String status;
    private String technicalGroupCode;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
//    private List<Topic> topics;
}
