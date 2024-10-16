package org.fms.training.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.common.enums.GroupStatus;

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
        return switch (dbData) {
            case "Planning" -> GroupStatus.PLANNING;
            case "Assigned" -> GroupStatus.ASSIGNED;
            case "Reviewing" -> GroupStatus.REVIEWING;
            case "Declined" -> GroupStatus.DECLINED;
            case "In Progress" -> GroupStatus.IN_PROGRESS;
            case "Pending Close" -> GroupStatus.PENDING_CLOSE;
            case "Closed" -> GroupStatus.CLOSED;
            default -> null;
        };
    }
}
