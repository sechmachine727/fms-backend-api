package org.fms.training.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
    private String account;
    private String password;
    private Status status;
}
