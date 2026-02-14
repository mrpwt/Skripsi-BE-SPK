package com.skripsi.spk.kriteria.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KriteriaResponseDto {
    private Long id;
    private String kode;
    private String namaKriteria;
    private BigDecimal bobot;
    private String tipe;
}
