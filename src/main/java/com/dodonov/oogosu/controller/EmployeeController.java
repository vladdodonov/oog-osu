package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.config.security.UserRole;
import com.dodonov.oogosu.domain.enums.Qualification;
import com.dodonov.oogosu.dto.EmployeeDto;
import com.dodonov.oogosu.dto.EmployeeSaveDto;
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

import java.util.Arrays;
import java.util.stream.Collectors;


@Api(tags = "employee", description = "Работа с работниками")
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @ApiOperation(value = "Получение всех работников по департаменту")
    @GetMapping(value = "/{departmentId}")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).INSPECTOR})")
    public ResponseEntity<CollectionResponse<EmployeeDto>> getAllByDepartment(@PathVariable(name = "departmentId") Long departmentId) {
        var employees = EmployeeMapper.INSTANCE.toDtos(employeeService.findAllByDepartmentId(departmentId));
        return ResponseBuilder.success(employees);
    }

    @ApiOperation(value = "Получение всех работников по департаменту (с удаленными)")
    @GetMapping(value = "/with-deleted/{departmentId}")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).INSPECTOR})")
    public ResponseEntity<CollectionResponse<EmployeeDto>> getAllByDepartmentWithDeleted(@PathVariable(name = "departmentId") Long departmentId) {
        var employees = EmployeeMapper.INSTANCE.toDtos(employeeService.findAllByDepartmentIdWithDeleted(departmentId));
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

    @ApiOperation(value = "Получение всех исполнителей по департаменту авторизованного начальника (с удаленными)")
    @GetMapping("/with-deleted")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD, T(com.dodonov.oogosu.config.security.UserRole).INSPECTOR})")
    public ResponseEntity<CollectionResponse<EmployeeDto>> getAllFromMyDepartmentWithDeleted() {
        var employees = EmployeeMapper.INSTANCE.toDtos(employeeService.getAllFromMyDepartmentWithDeleted());
        return ResponseBuilder.success(employees);
    }

    @ApiOperation(value = "Получить текущего сотрудника")
    @GetMapping(value = "/current")
    public ResponseEntity<Response<EmployeeDto>> getAllByDepartment() {
        var employee = EmployeeMapper.INSTANCE.toDto(employeeService.getCurrent());
        return ResponseBuilder.success(employee);
    }

    @ApiOperation(value = "Сохранить нового или отредактировать существующего работника")
    @PostMapping
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity<Response<EmployeeDto>> saveDepartment(@RequestBody EmployeeSaveDto dto) {
        return ResponseBuilder.success(EmployeeMapper.INSTANCE.toDto(employeeService.save(dto)));
    }

    @ApiOperation(value = "Сменить начальника в департаменте")
    @PostMapping("/change-lead")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity<Response<EmployeeDto>> changeLead(@RequestParam Long employeeId) {
        return ResponseBuilder.success(EmployeeMapper.INSTANCE.toDto(employeeService.changeLead(employeeId)));
    }

    @ApiOperation(value = "Удалить Работника")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity deleteEmployee(@PathVariable(value = "id") final Long departmentId) {
        employeeService.deleteById(departmentId);
        return ResponseBuilder.success();
    }

    @ApiOperation(value = "восстановить Работника")
    @PostMapping(value = "/restore/{id}")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity restoreEmployee(@PathVariable(value = "id") final Long departmentId) {
        return ResponseBuilder.success(EmployeeMapper.INSTANCE.toDto(employeeService.restore(departmentId)));
    }


    @ApiOperation(value = "Получить квалификацию")
    @GetMapping(value = "/qualifications")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD, T(com.dodonov.oogosu.config.security.UserRole).INSPECTOR})")
    public ResponseEntity<CollectionResponse<Qualification>> getDecisions() {
        return ResponseBuilder.success(Arrays.stream(Qualification.values()).filter(a -> !Qualification.SPECIAL.equals(a)).collect(Collectors.toList()));
    }


    @ApiOperation(value = "Получить роли")
    @GetMapping(value = "/roles")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).LEAD, T(com.dodonov.oogosu.config.security.UserRole).INSPECTOR})")
    public ResponseEntity<CollectionResponse<UserRole>> getRoles() {
        return ResponseBuilder.success(Arrays.asList(UserRole.values()));
    }
}
