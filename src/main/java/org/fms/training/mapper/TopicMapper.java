package org.fms.training.mapper;

import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    @Mapping(source = "topicName", target = "name")
    @Mapping(source = "topicCode", target = "code")
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "lastModifiedDate", target = "modifiedDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ListTopicDTO toListDTO(Topic topic);
}
