package com.skripsi.spk.soaltest.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "konfigurasi_ujian")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KonfigurasiUjian {

    @Id
    private String id; // Di-hardcode "TOTAL_SOAL" agar hanya ada 1 baris record data

    @Column(name = "nilai_konfigurasi", nullable = false)
    private Integer nilaiKonfigurasi;
}