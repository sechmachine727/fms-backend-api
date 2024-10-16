package org.fms.training.common.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicInfoDTO {
    private Integer id;
    private String topicCode;
    private String version;
    private String topicName;
}
