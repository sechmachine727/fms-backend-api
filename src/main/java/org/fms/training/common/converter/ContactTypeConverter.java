package org.fms.training.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.common.enums.ContractType;

@Converter
public class ContactTypeConverter implements AttributeConverter<ContractType, String> {
    @Override
    public String convertToDatabaseColumn(ContractType attribute) {
        return switch (attribute) {
            case OFFICIAL -> "Official";
            case COLLABORATOR -> "Collaborator";
        };
    }

    @Override
    public ContractType convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "Official" -> ContractType.OFFICIAL;
            case "Collaborator" -> ContractType.COLLABORATOR;
            default -> null;
        };
    }
}
