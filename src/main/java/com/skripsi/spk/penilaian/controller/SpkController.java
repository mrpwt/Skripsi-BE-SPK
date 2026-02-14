package com.skripsi.spk.penilaian.controller;

import com.skripsi.spk.penilaian.service.interfaces.SpkCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spk")
@RequiredArgsConstructor
public class SpkController {

    private final SpkCalculationService spkService;

    @PostMapping("/hitung/{nim}")
    public ResponseEntity<?> hitung(@PathVariable String nim) {
        try {
            // Hasil sudah berupa List<HasilRekomendasiResponse>
            var result = spkService.hitungRekomendasi(nim);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Terjadi kesalahan: " + e.getMessage());
        }
    }
}