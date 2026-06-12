package com.skripsi.spk.penilaian.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_rekomendasi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryRekomendasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nim;

    @Column(name = "nama_mahasiswa")
    private String namaMahasiswa;

    @Column(name = "bidang_rekomendasi", nullable = false)
    private String bidangRekomendasi;

    @Column(name = "tanggal_penilaian")
    private LocalDateTime tanggalPenilaian;

    @Column(name = "detail_skor", columnDefinition = "TEXT", nullable = false)
    private String detailSkor; // String raw JSON dari seluruh hasil perankingan SPK
}