package com.dodonov.oogosu.domain.dict;

import com.dodonov.oogosu.domain.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "d_department")
public class Department extends BaseEntity {
    @Column(name = "name")
    private String name;
    @OneToOne
    @JoinColumn(name = "lead_id", referencedColumnName = "id")
    private Employee lead;
}
