package org.fms.training.mapper;

import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.dto.topicdto.TopicDetailDTO;
import org.fms.training.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Mapper(componentModel = "spring",uses = {UnitMapper.class})
public interface TopicMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "topicName", target = "name")
    @Mapping(source = "topicCode", target = "code")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "lastModifiedBy", target = "lastModifiedBy")
    @Mapping(source = "lastModifiedDate", target = "modifiedDate", qualifiedByName = "formatDateToCustom")
    ListTopicDTO toListDTO(Topic topic);

    @Named("formatDateToCustom")
    static String formatDateToCustom(LocalDateTime lastModifiedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        return lastModifiedDate != null ? lastModifiedDate.format(formatter) : null;
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "topicCode", target = "code")
    @Mapping(source = "topicName", target = "name")
    @Mapping(source = "passCriteria", target = "passCriteria")
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "units", target = "units")
    TopicDetailDTO toDetailDTO(Topic topic);
}
