package org.fms.training.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.common.enums.TrainingProgramStatus;

@Converter
public class TrainingProgramStatusConverter implements AttributeConverter<TrainingProgramStatus, String> {
    @Override
    public String convertToDatabaseColumn(TrainingProgramStatus attribute) {
        return switch (attribute) {
            case ACTIVE -> "Active";
            case INACTIVE -> "Inactive";
            case REVIEWING -> "Reviewing";
            case DECLINED -> "Declined";
        };
    }

    @Override
    public TrainingProgramStatus convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "Active" -> TrainingProgramStatus.ACTIVE;
            case "Inactive" -> TrainingProgramStatus.INACTIVE;
            case "Reviewing" -> TrainingProgramStatus.REVIEWING;
            case "Declined" -> TrainingProgramStatus.DECLINED;
            default -> null;
        };
    }
}
