package org.fms.training.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.enums.TopicStatus;

@Converter
public class ActiveAndInactiveStatusConverter implements AttributeConverter<TopicStatus, String> {
    @Override
    public String convertToDatabaseColumn(TopicStatus attribute) {
        return switch (attribute) {
            case ACTIVE -> "Active";
            case INACTIVE -> "Inactive";
        };
    }

    @Override
    public TopicStatus convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "Active" -> TopicStatus.ACTIVE;
            case "Inactive" -> TopicStatus.INACTIVE;
            default -> null;
        };
    }
}
