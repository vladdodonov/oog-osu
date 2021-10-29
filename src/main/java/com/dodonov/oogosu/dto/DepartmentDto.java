package com.dodonov.oogosu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "DepartmentDto", description = "DTO департамента")
public class DepartmentDto {
    @ApiModelProperty(value = "Идентификатор")
    private Long id;

    @ApiModelProperty(value = "Название")
    private String name;
}
