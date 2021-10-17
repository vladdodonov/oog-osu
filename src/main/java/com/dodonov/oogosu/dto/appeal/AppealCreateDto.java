package com.dodonov.oogosu.dto.appeal;

import com.dodonov.oogosu.dto.CitizenDto;
import com.dodonov.oogosu.dto.TopicDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "AppealCreateDto", description = "DTO для создания обращения")
public class AppealCreateDto {
    @ApiModelProperty(value = "Идентификатор")
    private Long id;

    @ApiModelProperty(value = "Заявитель")
    private CitizenDto citizen;

    @ApiModelProperty(value = "Текст обращения")
    private String question;

    @ApiModelProperty(value = "Тема обращения")
    private TopicDto topic;

}
