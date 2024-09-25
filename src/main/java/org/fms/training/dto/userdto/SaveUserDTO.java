package org.fms.training.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.ContactType;
import org.fms.training.enums.Status;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserDTO {
    private String account;
    private String email;
    private String employeeId;
    private Status status;
    private ContactType contactType;
    private String department;
    private List<Integer> roles;
}
