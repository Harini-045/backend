package com.application.entity;

import com.application.config.Constants;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Constants.ERole name;

    @Column(columnDefinition = "BOOLEAN")
    private boolean isActive;

}
