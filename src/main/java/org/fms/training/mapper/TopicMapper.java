package org.fms.training.mapper;

import org.fms.training.dto.topicdto.ListTopicDTO;
import org.fms.training.dto.topicdto.TopicDetailDTO;
import org.fms.training.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UnitMapper.class})
public interface TopicMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "topicName", target = "name")
    @Mapping(source = "topicCode", target = "code")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "lastModifiedBy", target = "lastModifiedBy")
    @Mapping(source = "lastModifiedDate", target = "modifiedDate", dateFormat = "dd-MMM-YYYY")
    ListTopicDTO toListDTO(Topic topic);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "topicCode", target = "code")
    @Mapping(source = "topicName", target = "name")
    @Mapping(source = "passCriteria", target = "passCriteria")
    @Mapping(source = "technicalGroup.code", target = "technicalGroupCode")
    @Mapping(source = "units", target = "units")
    TopicDetailDTO toDetailDTO(Topic topic);
}
