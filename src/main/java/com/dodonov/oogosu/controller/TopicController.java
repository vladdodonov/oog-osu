package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.dto.TopicDto;
import com.dodonov.oogosu.mapstruct.TopicDtoMapper;
import com.dodonov.oogosu.service.TopicService;
import com.dodonov.oogosu.utils.http.CollectionResponse;
import com.dodonov.oogosu.utils.http.Response;
import com.dodonov.oogosu.utils.http.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Api(tags = "topic", description = "Работа с темами обращений")
@RestController
@RequestMapping("/api/topic")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @ApiOperation(value = "Получение тем обращения")
    @GetMapping(value = "/find-all")
    public ResponseEntity<CollectionResponse<TopicDto>> getAllTopics() {
        var topics = TopicDtoMapper.INSTANCE.toDtos(topicService.findAll());
        return ResponseBuilder.success(topics);
    }

    @ApiOperation(value = "Получение тем обращения")
    @GetMapping(value = "/find-all-with-deleted")
    public ResponseEntity<CollectionResponse<TopicDto>> getAllTopicsWithDeleted() {
        var topics = TopicDtoMapper.INSTANCE.toDtos(topicService.findAllWithDeleted());
        return ResponseBuilder.success(topics);
    }

    @ApiOperation(value = "Создать новую тему и добавить к департаменту")
    @PostMapping
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity<Response<TopicDto>> addTopicToDepartment(@RequestBody TopicDto dto) {
        var topic = TopicDtoMapper.INSTANCE.toEntity(dto);
        return ResponseBuilder.success(TopicDtoMapper.INSTANCE.toDto(topicService.addTopicToDepartment(topic)));
    }

    @ApiOperation(value = "Изменить название темы")
    @PostMapping(value = "/change-name")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity<Response<TopicDto>> changeTopicName(@RequestBody TopicDto dto) {
        return ResponseBuilder.success(TopicDtoMapper.INSTANCE.toDto(topicService.changeTopicName(dto)));
    }

    @ApiOperation(value = "Получение тем по id департамента")
    @GetMapping(value = "/{departmentId}")
    public ResponseEntity<CollectionResponse<TopicDto>> getAllByDepartment(@PathVariable(value = "departmentId") final Long departmentId) {
        return ResponseBuilder.success(TopicDtoMapper.INSTANCE.toDtos(topicService.findAllByDepartment(Department.builder().id(departmentId).build())));
    }

    @ApiOperation(value = "Удалить тему")
    @DeleteMapping(value = "/{topicId}")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity deleteTopic(@PathVariable(value = "topicId") final Long topicId) {
        topicService.deleteById(topicId);
        return ResponseBuilder.success();
    }

    @ApiOperation(value = "Восстановить тему")
    @PostMapping(value = "/restore/{topicId}")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity restoreTopic(@PathVariable(value = "topicId") final Long topicId) {
        return ResponseBuilder.success(topicService.restore(topicId));
    }
}
