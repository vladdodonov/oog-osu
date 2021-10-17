package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.dto.AppealCreateDto;
import com.dodonov.oogosu.mapstruct.appeal.AppealCreateDtoMapper;
import com.dodonov.oogosu.service.AppealService;
import com.dodonov.oogosu.utils.http.Response;
import com.dodonov.oogosu.utils.http.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "appeal", description = "Работа с обращениями")
@RestController
@RequestMapping("/api/appeal")
@RequiredArgsConstructor
public class AppealController {
    private final AppealService appealService;

    @ApiOperation(value = "Создание обращения")
    @PostMapping(value = "/create")
    public ResponseEntity<Response<Long>> createAppeal(@RequestBody AppealCreateDto dto) {
        var appeal = AppealCreateDtoMapper.INSTANCE.toEntity(dto);
        return ResponseBuilder.success(appealService.createAppeal(appeal));
    }
}
