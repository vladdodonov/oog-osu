package com.dodonov.oogosu.dto.appeal;

import com.dodonov.oogosu.domain.enums.Decision;
import com.dodonov.oogosu.domain.enums.State;
import com.dodonov.oogosu.dto.CitizenDto;
import com.dodonov.oogosu.dto.DepartmentDto;
import com.dodonov.oogosu.dto.EmployeeDto;
import com.dodonov.oogosu.dto.TopicDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "AppealDto", description = "DTO обращения")
public class AppealDto {
    @ApiModelProperty(value = "Идентификатор обращения")
    private Long id;

    @ApiModelProperty(value = "Заявитель")
    private CitizenDto citizen;

    @ApiModelProperty(value = "Вопрос")
    private String question;

    @ApiModelProperty(value = "Ответ")
    private String answer;

    @ApiModelProperty(value = "Решение")
    private Decision decision;

    @ApiModelProperty(value = "Департамент")
    private DepartmentDto department;

    @ApiModelProperty(value = "Исполнитель")
    private EmployeeDto executor;

    @ApiModelProperty(value = "Статус обращения")
    private State state;

    @ApiModelProperty(value = "Дата создания")
    private LocalDateTime creationDate;

    @ApiModelProperty(value = "Срок")
    private LocalDateTime dueDate;

    @ApiModelProperty(value = "Дата ответа")
    private LocalDateTime answerDate;

    @ApiModelProperty(value = "Признак продления")
    private Boolean isProlonged;

    @ApiModelProperty(value = "Тема обращения")
    private TopicDto topic;
}
