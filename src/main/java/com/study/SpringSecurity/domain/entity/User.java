package com.study.SpringSecurity.domain.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity //table
@Data
@Builder
public class User {
    @Id //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AI
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
}
