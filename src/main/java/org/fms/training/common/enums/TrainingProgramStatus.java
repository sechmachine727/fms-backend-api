package org.fms.training.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum TrainingProgramStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    REVIEWING("Reviewing"),
    DECLINED("Declined");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
