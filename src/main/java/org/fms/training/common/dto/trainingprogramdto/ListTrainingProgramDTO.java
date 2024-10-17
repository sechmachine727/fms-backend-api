package org.fms.training.common.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.dto.departmentdto.DepartmentDTO;
import org.fms.training.common.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.common.enums.TrainingProgramStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class ListTrainingProgramDTO {
    private Integer id;
    private String trainingProgramName;
    private String code;
    private String version;
    private DepartmentDTO department;
    private String description;
    private TrainingProgramStatus status;
    private ListTechnicalGroupDTO technicalGroup;
    private String note;
    private String lastModifiedBy;
    private String modifiedDate;
    private List<TopicInfoDTO> topicInfoList;
}
