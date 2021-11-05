package com.dodonov.oogosu.config.security;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "principal")
public class Principal {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    @Column(name = "archived")
    private Boolean archived;
}
