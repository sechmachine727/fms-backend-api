package org.fms.training.dto.trainingprogramdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.dto.departmentdto.DepartmentDTO;
import org.fms.training.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.enums.Status;

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
    private Status status;
    private ListTechnicalGroupDTO technicalGroup;
    private String lastModifiedBy;
    private String modifiedDate;
    private List<TopicInfoDTO> topicInfoList;
}
