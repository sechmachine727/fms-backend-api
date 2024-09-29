package org.fms.training.dto.groupdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.GroupStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class ListGroupDTO {
    private Integer id;
    private String groupName;
    private String groupCode;
    private Integer traineeNumber;
    private Integer trainingProgramId;
    private String trainingProgramName;
    private String siteName;
    private List<String> classAdminEmployeeId;
    private String expectedStartDate;
    private String expectedEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private GroupStatus status;

    //TODO: Make display classAdminEmployeeId work
}
