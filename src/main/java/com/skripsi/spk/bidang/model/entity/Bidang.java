package com.skripsi.spk.bidang.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bidang")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bidang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String kode;

    @Column(name = "nama_bidang", length = 100, nullable = false)
    private String namaBidang;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}