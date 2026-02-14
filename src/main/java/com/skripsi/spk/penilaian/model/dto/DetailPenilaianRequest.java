package com.skripsi.spk.penilaian.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailPenilaianRequest {
    private Long kriteriaId;
    private Double nilai;
}
