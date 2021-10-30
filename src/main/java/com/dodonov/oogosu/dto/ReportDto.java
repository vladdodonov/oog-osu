package com.dodonov.oogosu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "ReportDto", description = "Данные отчета по эффективности со средним критерием")
public class ReportDto {
    @ApiModelProperty(value = "Данные по департаментам")
    private List<DepartmentData> departmentDataList;
    @ApiModelProperty(value = "Среднее значение критерия")
    private Double averageCriteria;
}
