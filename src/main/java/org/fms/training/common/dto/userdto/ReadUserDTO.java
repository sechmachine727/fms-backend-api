package org.fms.training.common.dto.userdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.dto.departmentdto.DepartmentDTO;
import org.fms.training.common.enums.ContractType;
import org.fms.training.common.enums.Status;

import java.util.List;

@Data
@NoArgsConstructor
public class ReadUserDTO {
    private Integer id;
    private String account;
    private String email;
    private String name;
    private String employeeId;
    private Status status;
    private ContractType contractType;
    private DepartmentDTO department;
    private List<RoleDTO> roles;
}
