package com.application.entity;

import com.application.entity.UserEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

@ToString
@Data
@Entity(name = "refreshtoken")
@Table(name = "refreshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
