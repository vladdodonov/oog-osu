package com.dodonov.oogosu.domain.dict;

import com.dodonov.oogosu.domain.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "d_position")
public class Position extends BaseEntity {
    @Column(name = "name")
    private String name;
}
