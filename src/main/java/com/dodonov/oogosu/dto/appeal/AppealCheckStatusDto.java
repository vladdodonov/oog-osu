package com.dodonov.oogosu.dto.appeal;

import com.dodonov.oogosu.domain.enums.State;
import com.dodonov.oogosu.dto.CitizenDto;
import com.dodonov.oogosu.dto.TopicDto;
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
@ApiModel(value = "AppealCheckStatusDto", description = "DTO для проверки статуса обращения")
public class AppealCheckStatusDto {
    @ApiModelProperty(value = "Идентификатор обращения")
    private Long id;

    @ApiModelProperty(value = "Фамилия заявителя")
    private String citizenLastName;

    @ApiModelProperty(value = "Статус обращения")
    private State state;

}
