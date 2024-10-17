package org.fms.training.common.dto.traineedto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fms.training.common.enums.Gender;

@Data
@NoArgsConstructor
public class ListTraineeDTO {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private Gender gender;
    private String dob;
    private String university;
}
