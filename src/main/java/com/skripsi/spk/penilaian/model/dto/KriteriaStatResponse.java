package com.skripsi.spk.penilaian.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KriteriaStatResponse {
    private String kode;     // Contoh: "C1"
    private String nama;     // Contoh: "Nilai Mata Kuliah Terkait"
    private double rataRata; // Contoh: 83.3
}
