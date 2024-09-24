package org.fms.training.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum ContactType {
    OFFICIAL("Official"),
    COLLABORATOR("Collaborator");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
