package org.fms.training.dto.userdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.ContractType;
import org.fms.training.enums.Status;

import java.util.List;

@Data
@NoArgsConstructor
public class ReadUserDTO {
    private Integer id;
    private String account;
    private String email;
    private String employeeId;
    private Status status;
    private ContractType contractType;
    private Integer departmentId;
    private String departmentName;
    private List<Integer> roleIds;
    private List<String> roleNames;
}
