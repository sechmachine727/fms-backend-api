package org.fms.training.common.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.enums.TrainingProgramStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class SaveTrainingProgramDTO {
    private String trainingProgramName;
    private String code;
    private Integer departmentId;
    private Integer version;
    private String description;
    private TrainingProgramStatus status;
    private Integer technicalGroupId;
    private List<Integer> topicIds;
}