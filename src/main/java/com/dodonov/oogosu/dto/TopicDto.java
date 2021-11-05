package com.dodonov.oogosu.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopicDto {
    @ApiModelProperty(value = "Идентификатор")
    private Long id;
    @ApiModelProperty(value = "Наименование")
    private String name;
    @ApiModelProperty(value = "Архивировано")
    private Boolean archived;
    @ApiModelProperty(value = "Департамент")
    private DepartmentDto department;
}
