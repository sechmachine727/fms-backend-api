package org.fms.training.common.mapper;

import org.fms.training.common.dto.topicdto.ListTopicDTO;
import org.fms.training.common.dto.topicdto.TopicDetailDTO;
import org.fms.training.common.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UnitMapper.class})
public interface TopicMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "topicName", target = "name")
    @Mapping(source = "topicCode", target = "code")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "technicalGroup", target = "technicalGroup")
    @Mapping(source = "lastModifiedBy", target = "lastModifiedBy")
    @Mapping(source = "lastModifiedDate", target = "modifiedDate", dateFormat = "dd-MM-YYYY")
    ListTopicDTO toListDTO(Topic topic);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "topicCode", target = "code")
    @Mapping(source = "topicName", target = "name")
    @Mapping(source = "passCriteria", target = "passCriteria")
    @Mapping(source = "technicalGroup", target = "technicalGroup")
    @Mapping(source = "units", target = "units")
    TopicDetailDTO toDetailDTO(Topic topic);
}
