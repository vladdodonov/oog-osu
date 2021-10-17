package com.dodonov.oogosu.domain.dict;

import com.dodonov.oogosu.domain.BaseEntity;
import com.dodonov.oogosu.domain.enums.Qualification;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "d_employee")
public class Employee extends BaseEntity {
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    @Column(name = "qualification")
    @Enumerated(value = EnumType.STRING)
    private Qualification qualification;
    @Column(name = "username")
    private String username;
}
