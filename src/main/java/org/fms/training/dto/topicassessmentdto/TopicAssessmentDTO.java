package org.fms.training.dto.topicassessmentdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicAssessmentDTO {
    private String assessmentName;
    private Integer quantity;
    private Integer weightedNumber;
    private String note;
}
