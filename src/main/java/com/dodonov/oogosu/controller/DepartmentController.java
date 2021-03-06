package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.dto.DepartmentDto;
import com.dodonov.oogosu.dto.EmployeeDto;
import com.dodonov.oogosu.mapstruct.DepartmentMapper;
import com.dodonov.oogosu.mapstruct.EmployeeMapper;
import com.dodonov.oogosu.service.DepartmentService;
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

import java.util.Comparator;
import java.util.stream.Collectors;


@Api(tags = "department", description = "Работа с департаментами")
@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @ApiOperation(value = "Получение всех департаментов")
    @GetMapping(value = "/find-all")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).INSPECTOR})")
    public ResponseEntity<CollectionResponse<DepartmentDto>> getAllDepartments() {
        var departmentDtos = DepartmentMapper.INSTANCE.toDtos(departmentService.findAll());
        return ResponseBuilder.success(departmentDtos.stream().sorted(Comparator.comparing(DepartmentDto::getId)).collect(Collectors.toList()));
    }

    @ApiOperation(value = "Получение всех департаментов (с удаленными)")
    @GetMapping(value = "/find-all-with-deleted")
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).INSPECTOR})")
    public ResponseEntity<CollectionResponse<DepartmentDto>> getAllDepartmentsWithDeleted() {
        var departmentDtos = DepartmentMapper.INSTANCE.toDtos(departmentService.findAllWithDeleted());
        return ResponseBuilder.success(departmentDtos.stream().sorted(Comparator.comparing(DepartmentDto::getId)).collect(Collectors.toList()));
    }

    @ApiOperation(value = "Получить начальника департамента")
    @GetMapping(value = "/find-lead/{departmentId}")
    public ResponseEntity<Response<EmployeeDto>> findLead(@PathVariable(value = "departmentId") final Long departmentId) {
        var lead = EmployeeMapper.INSTANCE.toDto(employeeService.findLeadByDepartmentId(departmentId));
        return ResponseBuilder.success(lead);
    }

    @ApiOperation(value = "Сохранить департамент")
    @PostMapping
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity<Response<DepartmentDto>> saveDepartment(@RequestBody DepartmentDto dto) {
        var department = DepartmentMapper.INSTANCE.toEntity(dto);
        return ResponseBuilder.success(DepartmentMapper.INSTANCE.toDto(departmentService.saveDepartment(department)));
    }

    @ApiOperation(value = "Удалить департамент")
    @DeleteMapping(value = "/{departmentId}")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity deleteDepartment(@PathVariable(value = "departmentId") final Long departmentId) {
        departmentService.deleteById(departmentId);
        return ResponseBuilder.success();
    }

    @ApiOperation(value = "Восстановить департамент")
    @PostMapping(value = "/restore/{departmentId}")
    @PreAuthorize("hasRole(T(com.dodonov.oogosu.config.security.UserRole).ADMIN)")
    public ResponseEntity restoreDepartment(@PathVariable(value = "departmentId") final Long departmentId) {
        return ResponseBuilder.success(DepartmentMapper.INSTANCE.toDto(departmentService.restore(departmentId)));
    }
}
