package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.Status;

import java.util.List;

@Data
@NoArgsConstructor
public class ReadTrainingProgramDTO {
    private Integer id;
    private String trainingProgramName;
    private String code;
    private String region;
    private String version;
    private String description;
    private Status status;
    private String technicalGroupCode;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    private List<TopicInfoDTO> topicInfoList;
}
