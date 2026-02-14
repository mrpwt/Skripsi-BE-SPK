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
public class KriteriaRequestDto {
    private Long id; // null = create, ada = update
    private String kode;
    private String namaKriteria;
    private BigDecimal bobot;
    private String tipe;
}
