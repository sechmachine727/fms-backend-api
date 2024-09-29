package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListTrainingProgramNameWithVersionDTO {
    private Integer id;
    private String trainingProgramNameWithVersion;
}
