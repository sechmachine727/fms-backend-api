package org.fms.training.common.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.enums.ContractType;
import org.fms.training.common.enums.Status;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserDTO {
    private String account;
    private String email;
    private String name;
    private String employeeId;
    private Status status;
    private ContractType contractType;
    private Integer departmentId;
    private List<Integer> roles;
}
