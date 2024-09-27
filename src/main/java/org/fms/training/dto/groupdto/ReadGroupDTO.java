package org.fms.training.dto.groupdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.GroupStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class ReadGroupDTO {
    private Integer id;
    private String groupName;
    private Integer traineeNumber;
    private Integer trainingProgramId;
    private String trainingProgramName;
    private String siteName;
    private String expectedStartDate;
    private String expectedEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private String note;
    private Double planRevenue;
    private String deliveryTypeName;
    private String traineeTypeName;
    private String scopeName;
    private String formatTypeName;
    private String keyProgramName;
    private GroupStatus status;
    private List<String> employeeIds;
    //TODO: Add trainee, action and calendar DTOs here to display
}
