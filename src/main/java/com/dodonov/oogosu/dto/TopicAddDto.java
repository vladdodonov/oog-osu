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
public class TopicAddDto {
    @ApiModelProperty(value = "Наименование темы")
    private String name;
    @ApiModelProperty(value = "ID Департамента")
    private Long departmentId;
}
