package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.dto.EmployeeDto;
import com.dodonov.oogosu.dto.appeal.AppealMatchingEmployeeDto;
import com.dodonov.oogosu.mapstruct.EmployeeMapper;
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


@Api(tags = "employee", description = "Работа с работниками")
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @ApiOperation(value = "Получение всех работников по департаменту")
    @GetMapping(value = "/{departmentId}")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity<CollectionResponse<EmployeeDto>> getAllByDepartment(@PathVariable(name = "departmentId") Long departmentId) {
        var employees = EmployeeMapper.INSTANCE.toDtos(employeeService.findAllByDepartmentId(departmentId));
        return ResponseBuilder.success(employees);
    }

    @ApiOperation(value = "Найти подходящих исполнителей")
    @PostMapping(value = "/find-employees-matching")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<CollectionResponse<EmployeeDto>> findEmployeesMatching(@RequestBody AppealMatchingEmployeeDto dto) {
        return ResponseBuilder.success(EmployeeMapper.INSTANCE.toDtos(employeeService.findEmployeesMatching(dto)));
    }

    @ApiOperation(value = "Получение всех исполнителей по департаменту авторизованного начальника")
    @GetMapping
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD})")
    public ResponseEntity<CollectionResponse<EmployeeDto>> getAllFromMyDepartment() {
        var employees = EmployeeMapper.INSTANCE.toDtos(employeeService.getAllFromMyDepartment());
        return ResponseBuilder.success(employees);
    }

    @ApiOperation(value = "Получить текущего сотрудника")
    @GetMapping(value = "/current")
    public ResponseEntity<Response<EmployeeDto>> getAllByDepartment() {
        var employee = EmployeeMapper.INSTANCE.toDto(employeeService.getCurrent());
        return ResponseBuilder.success(employee);
    }

    @ApiOperation(value = "Сохранить работника")
    @PostMapping
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity<Response<EmployeeDto>> saveDepartment(@RequestBody EmployeeDto dto) {
        var employee = EmployeeMapper.INSTANCE.toEntity(dto);
        return ResponseBuilder.success(EmployeeMapper.INSTANCE.toDto(employeeService.save(employee)));
    }

    @ApiOperation(value = "Удалить Работника")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity deleteTopic(@PathVariable(value = "id") final Long departmentId) {
        employeeService.deleteById(departmentId);
        return ResponseBuilder.success();
    }
}
