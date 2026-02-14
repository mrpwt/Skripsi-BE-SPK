package com.skripsi.spk.kriteria.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "kriteria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Kriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5, nullable = false)
    private String kode;

    @Column(name = "nama_kriteria", length = 100, nullable = false)
    private String namaKriteria;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal bobot;

    @Column(length = 10)
    private String tipe;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
