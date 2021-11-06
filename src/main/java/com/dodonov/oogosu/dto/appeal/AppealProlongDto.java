package com.dodonov.oogosu.dto.appeal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "AppealProlongDto", description = "DTO для продления")
public class AppealProlongDto {
    @ApiModelProperty(value = "Идентификатор обращения")
    private Long id;

    @ApiModelProperty(value = "Новый срок")
    private LocalDateTime dueDate;

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atStartOfDay();;
    }
}
