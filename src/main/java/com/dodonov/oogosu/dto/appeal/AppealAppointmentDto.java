package com.dodonov.oogosu.dto.appeal;

import com.dodonov.oogosu.domain.enums.Difficulty;
import com.dodonov.oogosu.dto.EmployeeDto;
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
@ApiModel(value = "AppealAppointmentDto", description = "DTO для назначения на сотрудника обращения")
public class AppealAppointmentDto {
    @ApiModelProperty(value = "Идентификатор обращения")
    private Long id;
    @ApiModelProperty(value = "Срок")
    private LocalDateTime dueDate;
    @ApiModelProperty(value = "Сложность")
    private Difficulty difficulty;
    @ApiModelProperty(value = "Исполнитель")
    private EmployeeDto executor;
    @ApiModelProperty(value = "Признак жалобы")
    private Boolean isComplaint;
    public void setDueDate(ZonedDateTime dueDate) {
        System.out.println(dueDate);
        this.dueDate = dueDate.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
        System.out.println(this.dueDate);
    }
}
