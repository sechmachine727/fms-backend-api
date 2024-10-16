package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.dto.departmentdto.DepartmentDTO;
import org.fms.training.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.enums.TrainingProgramStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class ReadTrainingProgramDTO {
    private Integer id;
    private String trainingProgramName;
    private String code;
    private DepartmentDTO department;
    private String version;
    private String description;
    private TrainingProgramStatus status;
    private ListTechnicalGroupDTO technicalGroup;
    private String createdDate;
    private String note;
    private String lastModifiedBy;
    private String lastModifiedDate;
    private List<TopicInfoDTO> topicInfoList;
}
