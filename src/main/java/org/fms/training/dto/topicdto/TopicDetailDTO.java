package org.fms.training.dto.topicdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.dto.topicassessmentdto.TopicAssessmentDTO;
import org.fms.training.dto.unitdto.UnitDTO;

import java.util.List;

@Data
@NoArgsConstructor
public class TopicDetailDTO {
    private Integer id;
    private String code;
    private String name;
    private String passCriteria;
    private String technicalGroupCode;
    private List<UnitDTO> units;
    private List<TopicAssessmentDTO> topicAssessments;
}
