package com.skripsi.spk.login.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username; // NIM

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expired_at")
    private LocalDateTime resetTokenExpiredAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}


