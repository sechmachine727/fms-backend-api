package org.fms.training.dto.topicdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.enums.Status;

@Data
@NoArgsConstructor
public class ListTopicDTO {
    private Integer id;
    private String code;
    private String name;
    private String version;
    private Status status;
    private ListTechnicalGroupDTO technicalGroup;
    private String lastModifiedBy;
    private String modifiedDate;
}