package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.Status;

import java.util.List;

@Data
@NoArgsConstructor
public class SaveTrainingProgramDTO {
    private String trainingProgramName;
    private String code;
    private String region;
    private Integer version;
    private String description;
    private Status status;
    private Integer technicalGroupId;
    private List<Integer> topicIds;
}