package com.skripsi.spk.penilaian.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "hasil_rekomendasi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HasilRekomendasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nim", nullable = false)
    private String nim;

    // ID Jurusan (RPL=1, AI=2, Cyber=3)
    @Column(name = "bidang_id", nullable = false)
    private Integer bidangId;

    // Skor hasil perhitungan SAW (Preferensi V)
    @Column(name = "skor_akhir", precision = 10, scale = 4)
    private BigDecimal skorAkhir;

    // Juara 1, 2, 3
    @Column(name = "ranking")
    private Integer ranking;
}
