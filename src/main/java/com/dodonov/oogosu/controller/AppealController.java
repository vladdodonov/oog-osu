package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.domain.enums.Decision;
import com.dodonov.oogosu.domain.enums.Difficulty;
import com.dodonov.oogosu.dto.EmployeeDto;
import com.dodonov.oogosu.dto.appeal.*;
import com.dodonov.oogosu.mapstruct.EmployeeMapper;
import com.dodonov.oogosu.mapstruct.appeal.AppealCreateDtoMapper;
import com.dodonov.oogosu.mapstruct.appeal.AppealDtoMapper;
import com.dodonov.oogosu.service.AppealService;
import com.dodonov.oogosu.service.EmployeeService;
import com.dodonov.oogosu.utils.http.CollectionResponse;
import com.dodonov.oogosu.utils.http.Response;
import com.dodonov.oogosu.utils.http.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


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

    @ApiOperation(value = "Проверка статуса обращения")
    @PostMapping(value = "/check-status")
    public ResponseEntity<Response<AppealCheckStatusDto>> createAppeal(@RequestBody AppealCheckStatusDto dto) {
        return ResponseBuilder.success(appealService.checkStatus(dto));
    }

    @ApiOperation(value = "Найти обращения по фильтрам")
    @PostMapping(value = "/find-by-criteria")
    public ResponseEntity<CollectionResponse<AppealDto>> findByCriteria(@RequestBody AppealCriteria dto) {
        return ResponseBuilder.success(appealService.findByCriteria(dto));
    }

    @ApiOperation(value = "Назначить обращение на сотрудника")
    @PostMapping(value = "/appoint")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<Response<AppealDto>> appoint(@RequestBody AppealAppointmentDto dto) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.appoint(dto)));
    }

    @ApiOperation(value = "Продлить срок обращения")
    @PostMapping(value = "/prolong")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<Response<AppealDto>> prolong(@RequestBody AppealDto dto) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.prolong(dto.getId(), dto.getDueDate())));
    }

    @ApiOperation(value = "Подготовить ответ")
    @PostMapping(value = "/answer")
    public ResponseEntity<Response<AppealDto>> answer(@RequestBody AppealAnswerDto dto) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.answer(dto)));
    }

    @ApiOperation(value = "Вернуть на доработку")
    @PostMapping(value = "/return-to-executor")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<Response<AppealDto>> returnToExecutor(@RequestBody AppealReturnDto appealDto) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.returnToExecutor(appealDto)));
    }

    @ApiOperation(value = "Отправить ответ автору")
    @PostMapping(value = "/send-answer/{id}")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<Response<AppealDto>> sendAnswer(@PathVariable(name = "id") Long id) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.sendAnswer(id)));
    }

    @ApiOperation(value = "Получить сложность")
    @GetMapping(value = "/difficulties")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<CollectionResponse<Difficulty>> getDifficulties() {
        return ResponseBuilder.success(Arrays.asList(Difficulty.values()));
    }

    @ApiOperation(value = "Получить решение")
    @GetMapping(value = "/decisions")
    public ResponseEntity<CollectionResponse<Decision>> getDecisions() {
        return ResponseBuilder.success(Arrays.asList(Decision.values()));
    }
}
