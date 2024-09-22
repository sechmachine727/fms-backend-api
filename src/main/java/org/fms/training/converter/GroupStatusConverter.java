package org.fms.training.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.enums.GroupStatus;

@Converter
public class GroupStatusConverter implements AttributeConverter<GroupStatus, String> {
    @Override
    public String convertToDatabaseColumn(GroupStatus attribute) {
        return switch (attribute) {
            case PLANNING -> "Planning";
            case ASSIGNED -> "Assigned";
            case REVIEWING -> "Reviewing";
            case DECLINED -> "Declined";
            case IN_PROGRESS -> "In Progress";
            case PENDING_CLOSE -> "Pending Close";
            case CLOSED -> "Closed";
        };
    }

    @Override
    public GroupStatus convertToEntityAttribute(String dbData) {
        return null;
    }
}
