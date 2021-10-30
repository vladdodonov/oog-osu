package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.dto.EmployeeDto;
import com.dodonov.oogosu.dto.appeal.AppealCheckStatusDto;
import com.dodonov.oogosu.dto.appeal.AppealCreateDto;
import com.dodonov.oogosu.dto.appeal.AppealCriteria;
import com.dodonov.oogosu.dto.appeal.AppealDto;
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


@Api(tags = "appeal", description = "Работа с обращениями")
@RestController
@RequestMapping("/api/appeal")
@RequiredArgsConstructor
public class AppealController {
    private final AppealService appealService;
    private final EmployeeService employeeService;

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

    @ApiOperation(value = "Найти подходящих исполнителей")
    @PostMapping(value = "/find-employees-matching")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<CollectionResponse<EmployeeDto>> findEmployeesMatching(@RequestBody AppealDto dto) {
        return ResponseBuilder.success(EmployeeMapper.INSTANCE.toDtos(employeeService.findEmployeesMatching(dto)));
    }

    @ApiOperation(value = "Назначить обращение на сотрудника")
    @PostMapping(value = "/appoint/{id}")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<Response<AppealDto>> appoint(@RequestBody EmployeeDto dto, @PathVariable(name = "id") Long appealId, @RequestParam Boolean isComplaint) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.appoint(EmployeeMapper.INSTANCE.toEntity(dto), appealId, isComplaint)));
    }

    @ApiOperation(value = "Продлить срок обращения")
    @PostMapping(value = "/prolong")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<Response<AppealDto>> prolong(@RequestBody AppealDto dto) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.prolong(dto.getId(), dto.getDueDate())));
    }

    @ApiOperation(value = "Подготовить ответ")
    @PostMapping(value = "/answer")
    public ResponseEntity<Response<AppealDto>> answer(@RequestBody AppealDto appealDto) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.answer(appealDto)));
    }

    @ApiOperation(value = "Вернуть на доработку")
    @PostMapping(value = "/return-to-executor")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<Response<AppealDto>> returnToExecutor(@RequestBody AppealDto appealDto) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.returnToExecutor(appealDto)));
    }

    @ApiOperation(value = "Отправить ответ автору")
    @PostMapping(value = "/send-answer")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<Response<AppealDto>> sendAnswer(@RequestBody AppealDto appealDto) {
        return ResponseBuilder.success(AppealDtoMapper.INSTANCE.toDto(appealService.sendAnswer(appealDto)));
    }
}
