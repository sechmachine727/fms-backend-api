package org.fms.training.dto.userdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.ContactType;
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
    private ContactType contactType;
    private String department;
    private List<String> roleNames;
}
