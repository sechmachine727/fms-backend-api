package org.fms.training.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fms.training.enums.TraineeGroupStatusType;

@Converter
public class TraineeGroupStatusTypeConverter implements AttributeConverter<TraineeGroupStatusType, String> {
    @Override
    public String convertToDatabaseColumn(TraineeGroupStatusType attribute) {
        return switch (attribute) {
            case ACTIVE -> "Active";
            case PASSED -> "Passed";
            case DROPPED_OUT -> "Dropped Out";
            case DEFERRED -> "Deferred";
            case FAILED -> "Failed";
            case REJECTED -> "Rejected";
            case OJT -> "Ojt";
        };
    }

    @Override
    public TraineeGroupStatusType convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "Active" -> TraineeGroupStatusType.ACTIVE;
            case "Passed" -> TraineeGroupStatusType.PASSED;
            case "Dropped Out" -> TraineeGroupStatusType.DROPPED_OUT;
            case "Deferred" -> TraineeGroupStatusType.DEFERRED;
            case "Failed" -> TraineeGroupStatusType.FAILED;
            case "Rejected" -> TraineeGroupStatusType.REJECTED;
            case "Ojt" -> TraineeGroupStatusType.OJT;
            default -> null;
        };
    }
}
