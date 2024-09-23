package org.fms.training.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrainingProgramDTO {
    private String name;
    private String code;
    private String region;
    private Integer version;
    private String description;
    private String status;
    private String technicalGroupName;
    private String createdDate;
    private String lastModifiedBy;
    private String modifiedDate;
}
