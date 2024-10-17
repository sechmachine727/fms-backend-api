package org.fms.training.common.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String type;
    private String message;

    public AuthenticationResponse(String token, String type) {
        this.token = token;
        this.type = type;
    }

    public AuthenticationResponse(String message) {
        this.message = message;
    }
}
