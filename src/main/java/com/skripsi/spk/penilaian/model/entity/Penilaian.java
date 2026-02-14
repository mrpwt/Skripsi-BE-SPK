package com.skripsi.spk.penilaian.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "penilaian_mahasiswa",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"nim", "kriteria_id"}
        ))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Penilaian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nim")
    private String nim;

    @Column(name = "bidang_id")
    private Integer bidangId;

    @Column(name = "kriteria_id")
    private Long kriteriaId;

    @Column(name = "nilai")
    private Double nilai;
}

