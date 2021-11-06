package com.dodonov.oogosu.dto.appeal;

import com.dodonov.oogosu.domain.enums.Decision;
import com.dodonov.oogosu.domain.enums.Difficulty;
import com.dodonov.oogosu.domain.enums.State;
import com.dodonov.oogosu.dto.CitizenDto;
import com.dodonov.oogosu.dto.RangeDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "AppealCriterid", description = "DTO для поиска обращений")
public class AppealCriteria {
    @ApiModelProperty(value = "Заявитель")
    private CitizenDto citizen;
    @ApiModelProperty(value = "Разбег и сортировка")
    private RangeDto range;
    @ApiModelProperty(value = "Решения")
    private List<Decision> decisions;
    @ApiModelProperty(value = "Статусы")
    private List<State> states;
    @ApiModelProperty(value = "Идентификаторы департаментов")
    private List<Long> departmentIds;
    @ApiModelProperty(value = "Идентификаторы Обращений")
    private List<Long> appealIds;
    @ApiModelProperty(value = "Идентификаторы исполнителей")
    private List<Long> executorIds;
    @ApiModelProperty(value = "Дата создания от")
    private LocalDateTime creationDateFrom;
    @ApiModelProperty(value = "Дата создания до")
    private LocalDateTime creationDateTo;
    @ApiModelProperty(value = "Срок от")
    private LocalDateTime dueDateFrom;
    @ApiModelProperty(value = "Срок до")
    private LocalDateTime dueDateTo;
    @ApiModelProperty(value = "Дата ответа от")
    private LocalDateTime answerDateFrom;
    @ApiModelProperty(value = "Дата ответа до")
    private LocalDateTime answerDateTo;
    @ApiModelProperty(value = "Признак продленности")
    private Boolean isProlonged;
    @ApiModelProperty(value = "Идентификаторы тем")
    private List<Long> topicIds;
    @ApiModelProperty(value = "Сложность")
    private List<Difficulty> difficulties;
    @ApiModelProperty(value = "Признак жалобы")
    private Boolean isComplaint;
    @ApiModelProperty(value = "Признак возвращения")
    private Boolean isReturned;

    public void setCreationDateFrom(ZonedDateTime creationDateFrom) {
        this.creationDateFrom = creationDateFrom.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
    }

    public void setCreationDateTo(ZonedDateTime creationDateTo) {
        this.creationDateTo = creationDateTo.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX);
    }

    public void setDueDateFrom(ZonedDateTime dueDateFrom) {
        this.dueDateFrom = dueDateFrom.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atStartOfDay();;
    }

    public void setDueDateTo(ZonedDateTime dueDateTo) {
        this.dueDateTo = dueDateTo.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX);
    }

    public void setAnswerDateFrom(ZonedDateTime answerDateFrom) {
        this.answerDateFrom = answerDateFrom.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atStartOfDay();;
    }

    public void setAnswerDateTo(ZonedDateTime answerDateTo) {
        this.answerDateTo = answerDateTo.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX);
    }

    @Builder.Default
    @ApiModelProperty(hidden = true)
    private Set<String> fetch = new HashSet<>();

    public void setFetch(String... fetches) {
        this.fetch.addAll(Arrays.asList(fetches));
    }
}
