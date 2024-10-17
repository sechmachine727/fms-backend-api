package org.fms.training.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum TraineeGroupStatusType {
    ACTIVE("Active"),
    PASSED("Passed"),
    DROPPED_OUT("Dropped Out"),
    DEFERRED("Deferred"),
    FAILED("Failed"),
    REJECTED("Rejected"),
    OJT("OJT");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}