package org.fms.training.common.dto.topicdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.dto.unitdto.UnitDTO;
import org.fms.training.common.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.common.dto.topicassessmentdto.TopicAssessmentDTO;

import java.util.List;

@Data
@NoArgsConstructor
public class TopicDetailDTO {
    private Integer id;
    private String code;
    private String name;
    private String passCriteria;
    private ListTechnicalGroupDTO technicalGroup;
    private List<UnitDTO> units;
    private List<TopicAssessmentDTO> topicAssessments;
}
