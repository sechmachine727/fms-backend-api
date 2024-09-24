package org.fms.training.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
