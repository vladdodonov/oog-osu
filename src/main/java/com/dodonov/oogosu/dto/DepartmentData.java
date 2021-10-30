package com.dodonov.oogosu.dto;

import com.dodonov.oogosu.domain.enums.ColorOnAverage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "DepartmentData", description = "Данные отчета по эффективности по департаменту")
public class DepartmentData {
    @ApiModelProperty(value = "Идентификатор")
    private Long id;
    @ApiModelProperty(value = "Название департамента")
    private String name;
    @ApiModelProperty(value = "Число обращений")
    private Long numberOfAppeals;
    @ApiModelProperty(value = "Число обращений (частный показатель)")
    private Double numberOfAppealsNormalized;
    @ApiModelProperty(value = "Процент продленных обращений")
    private Double prolongedPercent;
    @ApiModelProperty(value = "Процент продленных обращений (частный показатель)")
    private Double prolongedPercentNormalized;
    @ApiModelProperty(value = "Процент удовлетворенных жалоб")
    private Double substantiatedComplaintsPercent;
    @ApiModelProperty(value = "Процент удовлетворенных жалоб (частный показатель)")
    private Double substantiatedComplaintsPercentNormalized;
    @ApiModelProperty(value = "Процент возвращенных на доработку обращений")
    private Double returnedOnRevisionPercent;
    @ApiModelProperty(value = "Процент возвращенных на доработку обращений (частный показатель)")
    private Double returnedOnRevisionPercentNormalized;
    @ApiModelProperty(value = "Критерий")
    private Double criteria;
    @ApiModelProperty(value = "Цвет строки")
    private ColorOnAverage color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentData that = (DepartmentData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
