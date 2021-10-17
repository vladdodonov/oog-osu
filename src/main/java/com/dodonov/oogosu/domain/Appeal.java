package com.dodonov.oogosu.domain;

import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.dict.Topic;
import com.dodonov.oogosu.domain.enums.Decision;
import com.dodonov.oogosu.domain.enums.State;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
