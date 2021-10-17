package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.dto.TopicDto;
import com.dodonov.oogosu.mapstruct.TopicDtoMapper;
import com.dodonov.oogosu.service.TopicService;
import com.dodonov.oogosu.utils.http.CollectionResponse;
import com.dodonov.oogosu.utils.http.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
