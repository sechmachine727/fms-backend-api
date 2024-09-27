package org.fms.training.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.enums.Gender;

@Converter
public class GenderConverter implements AttributeConverter<Gender, String> {

    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return switch (attribute) {
            case MALE -> "Male";
            case FEMALE -> "Female";
        };
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "Male" -> Gender.MALE;
            case "Female" -> Gender.FEMALE;
            default -> null;
        };
    }
}
