package org.fms.training.common.dto.groupdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.enums.GroupStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class ReadGroupDTO {
    private Integer id;
    private String groupName;
    private String groupCode;
    private Integer traineeNumber;
    private Integer trainingProgramId;
    private String trainingProgramName;
    private String technicalGroupCode;
    private Integer siteId;
    private String siteName;
    private Integer locationId;
    private String locationName;
    private String expectedStartDate;
    private String expectedEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private String note;
    private Integer deliveryTypeId;
    private String deliveryTypeName;
    private Integer traineeTypeId;
    private String traineeTypeName;
    private Integer scopeId;
    private String createdBy;
    private String scopeName;
    private Integer formatTypeId;
    private String formatTypeName;
    private Integer keyProgramId;
    private String keyProgramName;
    private GroupStatus status;
    private List<Integer> assignedUserIds;
    private List<String> assignedUserAccounts;
    //TODO: Add trainee, action and calendar DTOs here to display
}
