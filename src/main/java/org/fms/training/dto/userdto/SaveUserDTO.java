package org.fms.training.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.ContractType;
import org.fms.training.enums.Status;

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
