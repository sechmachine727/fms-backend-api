package org.fms.training.common.dto.groupdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.enums.GroupStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class ListGroupDTO {
    private Integer id;
    private String groupName;
    private String groupCode;
    private String technicalGroupCode;
    private Integer traineeNumber;
    private Integer trainingProgramId;
    private String trainingProgramName;
    private String traineeTypeName;
    private String siteName;
    private String locationName;
    private String note;
    private List<String> classAdminAccount;
    private String expectedStartDate;
    private String expectedEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private GroupStatus status;
}
