package com.skripsi.spk.penilaian.service.interfaces;

import com.skripsi.spk.penilaian.model.dto.HasilRekomendasiResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SpkCalculationService {
    List<HasilRekomendasiResponseDto> hitungRekomendasi(String nim);

    ResponseEntity<byte[]> generateRaportPdf(Long historyId);
}
