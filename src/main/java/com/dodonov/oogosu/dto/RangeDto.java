package com.dodonov.oogosu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "Range", description = "DTO для пагинации")
public class RangeDto {
    @ApiModelProperty(value = "Отступ")
    private Integer skip;
    @ApiModelProperty(value = "Сколько взять")
    private Integer take;
    @ApiModelProperty(value = "Порядок")
    private Sort.Direction direction;
}
