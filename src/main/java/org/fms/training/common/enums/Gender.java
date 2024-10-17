package org.fms.training.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
