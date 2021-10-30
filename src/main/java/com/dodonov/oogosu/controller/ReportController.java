package com.dodonov.oogosu.controller;

import com.dodonov.oogosu.dto.ReportDto;
import com.dodonov.oogosu.service.ReportService;
import com.dodonov.oogosu.utils.http.Response;
import com.dodonov.oogosu.utils.http.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "report", description = "Работа с отчетом по эффективности работы департаментов")
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @ApiOperation(value = "Получение отчета")
    @GetMapping
    @PreAuthorize("hasAnyRole({T(com.dodonov.oogosu.config.security.UserRole).ADMIN, T(com.dodonov.oogosu.config.security.UserRole).INSPECTOR})")
    public ResponseEntity<Response<ReportDto>> getReport() {
        return ResponseBuilder.success(reportService.getReport());
    }
}
