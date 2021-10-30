package com.dodonov.oogosu.dto.appeal;

import com.dodonov.oogosu.domain.enums.Difficulty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "AppealMatchingEmployeeDto", description = "DTO для поиска подходящего сотрудника")
public class AppealMatchingEmployeeDto {
    @ApiModelProperty(value = "Срок")
    private LocalDateTime dueDate;
    @ApiModelProperty(value = "Сложность")
    private Difficulty difficulty;
}
