package com.dodonov.oogosu.dto.appeal;

import com.dodonov.oogosu.domain.enums.Decision;
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
@ApiModel(value = "AppealReturnDto", description = "DTO для возврата обращения на доработку")
public class AppealReturnDto {
    @ApiModelProperty(value = "Идентификатор обращения")
    private Long id;
    @ApiModelProperty(value = "Причина возврата")
    private String returnReason;
    @ApiModelProperty(value = "Срок")
    private LocalDateTime dueDate;
}
