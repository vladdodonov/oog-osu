package com.dodonov.oogosu.mapstruct;

import com.dodonov.oogosu.domain.dict.Topic;
import com.dodonov.oogosu.dto.TopicDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {DepartmentMapper.class})
public interface TopicDtoMapper {
    TopicDtoMapper INSTANCE = Mappers.getMapper(TopicDtoMapper.class);
    Topic toEntity(TopicDto topicDto);
    TopicDto toDto(Topic topic);
    List<Topic> toEntities(List<TopicDto> topicDto);
    List<TopicDto> toDtos(List<Topic> topic);
}
