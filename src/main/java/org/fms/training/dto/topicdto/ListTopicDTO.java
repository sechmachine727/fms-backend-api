package org.fms.training.dto.topicdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListTopicDTO {
    private Integer id;
    private String code;
    private String name;
    private String version;
    private String status;
    private String technicalGroupCode;
    private String lastModifiedBy;
    private String modifiedDate;
}