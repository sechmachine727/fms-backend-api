package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SaveTrainingProgramDTO {
    private String name;
    private String code;
    private String region;
    private Integer version;
    private String description;
    private String status;
    private Integer technicalGroupId;
    private List<Integer> topicIds;
}