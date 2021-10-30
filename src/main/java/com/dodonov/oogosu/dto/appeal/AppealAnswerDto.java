package com.dodonov.oogosu.dto.appeal;

import com.dodonov.oogosu.domain.enums.Decision;
import com.dodonov.oogosu.domain.enums.Difficulty;
import com.dodonov.oogosu.dto.EmployeeDto;
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
@ApiModel(value = "AppealAnswerDto", description = "DTO для ответа")
public class AppealAnswerDto {
    @ApiModelProperty(value = "Идентификатор обращения")
    private Long id;
    @ApiModelProperty(value = "Решение")
    private Decision decision;
    @ApiModelProperty(value = "Ответ")
    private String answer;
}
