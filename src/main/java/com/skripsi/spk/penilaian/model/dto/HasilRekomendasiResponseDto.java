package com.skripsi.spk.penilaian.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HasilRekomendasiResponseDto {

    private String nim;
    private Integer ranking;
    private Integer bidangId;
    // Tambahkan namaBidang jika Anda ingin frontend langsung terima nama (misal "RPL")
    // private String namaBidang;
    private BigDecimal skorAkhir;

    private Map<String, Double> normalizedValues;
}
