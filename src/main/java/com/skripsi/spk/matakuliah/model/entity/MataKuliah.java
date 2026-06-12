package com.skripsi.spk.matakuliah.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "mata_kuliah")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MataKuliah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bidang_id", nullable = false)
    private Long bidangId;

    @Column(name = "nama_mata_kuliah", nullable = false)
    private String namaMataKuliah;
}
