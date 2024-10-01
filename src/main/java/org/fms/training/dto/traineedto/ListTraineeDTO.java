package org.fms.training.dto.traineedto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.enums.Gender;

@Data
@NoArgsConstructor
public class ListTraineeDTO {
    private String name;
    private String email;
    private String phone;
    private Gender gender;
    private String dob;
    private String university;
}
