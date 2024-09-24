package org.fms.training.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.enums.ContactType;

@Converter
public class ContactTypeConverter implements AttributeConverter<ContactType, String> {
    @Override
    public String convertToDatabaseColumn(ContactType attribute) {
        return switch (attribute) {
            case OFFICIAL -> "Official";
            case COLLABORATOR -> "Collaborator";
        };
    }

    @Override
    public ContactType convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "Official" -> ContactType.OFFICIAL;
            case "Collaborator" -> ContactType.COLLABORATOR;
            default -> null;
        };
    }
}
