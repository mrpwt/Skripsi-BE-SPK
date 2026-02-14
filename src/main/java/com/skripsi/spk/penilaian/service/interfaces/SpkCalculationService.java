package com.skripsi.spk.penilaian.service.interfaces;

import com.skripsi.spk.penilaian.model.dto.HasilRekomendasiResponseDto;

import java.util.List;

public interface SpkCalculationService {
    List<HasilRekomendasiResponseDto> hitungRekomendasi(String nim);
}
