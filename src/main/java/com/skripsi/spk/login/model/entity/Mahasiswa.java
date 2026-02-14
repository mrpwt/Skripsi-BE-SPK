package com.skripsi.spk.login.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mahasiswa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mahasiswa {
    @Id
    @Column(name = "nim", length = 20, nullable = false)
    private String nim;

    @Column(name = "nama_mahasiswa", length = 150, nullable = false)
    private String namaMahasiswa;

    @Column(name = "email", length = 100)
    private String email;

    // ==============================
    // RELASI KE USERS
    // ==============================

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
