package org.fms.training.common.dto.topicdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.dto.technicalgroupdto.ListTechnicalGroupDTO;
import org.fms.training.common.enums.Status;

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