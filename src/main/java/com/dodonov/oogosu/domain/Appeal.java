package com.dodonov.oogosu.domain;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.dict.Topic;
import com.dodonov.oogosu.domain.enums.Decision;
import com.dodonov.oogosu.domain.enums.Difficulty;
import com.dodonov.oogosu.domain.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appeal")
public class Appeal extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "citizen_id", referencedColumnName = "id")
    private Citizen citizen;
    @Column(name = "question")
    private String question;
    @Column(name = "answer")
    private String answer;
    @Column(name = "decision")
    @Enumerated(value = EnumType.STRING)
    private Decision decision;
    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state;
    @OneToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    @OneToOne
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private Employee executor;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    @Column(name = "answer_date")
    private LocalDateTime answerDate;
    @Column(name = "is_prolonged")
    private Boolean isProlonged;
    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id")
    private Topic topic;
    @Column(name = "difficulty")
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;
    @Column(name = "return_reason")
    private String returnReason;
    @Column(name = "is_complaint")
    private Boolean isComplaint;
    @Column(name = "is_returned")
    private Boolean isReturned;

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setAnswerDate(LocalDateTime answerDate) {
        this.answerDate = answerDate;
    }
}
