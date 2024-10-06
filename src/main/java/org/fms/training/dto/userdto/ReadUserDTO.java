package org.fms.training.dto.userdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.dto.contracttypedto.ContractTypeDTO;
import org.fms.training.dto.departmentdto.DepartmentDTO;
import org.fms.training.enums.Status;

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
    private ContractTypeDTO contractType;
    private DepartmentDTO department;
    private List<RoleDTO> roles;
}
