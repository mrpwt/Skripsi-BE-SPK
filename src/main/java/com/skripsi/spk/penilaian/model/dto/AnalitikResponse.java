package com.skripsi.spk.penilaian.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalitikResponse {
    private long totalMahasiswa;
    private long totalKriteria;
    private long totalBidang;
    private double rataRataNilai;
    private List<KriteriaStatResponse> rataRataPerKriteria;
}
