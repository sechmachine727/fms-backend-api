package org.fms.training.common.dto.groupdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.enums.GroupStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class SaveGroupDTO {
    private String groupName;
    private String groupCode;
    private Integer traineeNumber;
    private Integer trainingProgramId;
    private Integer technicalGroupId;
    private Integer siteId;
    private Integer locationId;
    private String expectedStartDate;
    private String actualEndDate;
    private Double planRevenue;
    private Integer deliveryTypeId;
    private Integer traineeTypeId;
    private Integer scopeId;
    private Integer formatTypeId;
    private Integer keyProgramId;
    private String note;
    private GroupStatus status;
    private List<Integer> assignedUserIds;
}
