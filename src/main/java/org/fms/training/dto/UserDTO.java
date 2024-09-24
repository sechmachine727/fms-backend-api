package org.fms.training.dto;

import lombok.*;
import org.fms.training.enums.ContactType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String account;
    private String email;
    private String employeeId;
    private boolean status;
    private ContactType contactType;
    private String department;
    private List<Integer> roles;
}
