package org.fms.training.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum GroupStatus {
    PLANNING("Planning"),
    ASSIGNED("Assigned"),
    REVIEWING("Reviewing"),
    DECLINED("Declined"),
    IN_PROGRESS("In Progress"),
    PENDING_CLOSE("Pending Close"),
    CLOSED("Closed");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}