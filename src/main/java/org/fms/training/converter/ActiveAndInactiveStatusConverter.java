package org.fms.training.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.enums.Status;

@Converter
public class ActiveAndInactiveStatusConverter implements AttributeConverter<Status, String> {
    @Override
    public String convertToDatabaseColumn(Status attribute) {
        return switch (attribute) {
            case ACTIVE -> "Active";
            case INACTIVE -> "Inactive";
        };
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "Active" -> Status.ACTIVE;
            case "Inactive" -> Status.INACTIVE;
            default -> null;
        };
    }
}
