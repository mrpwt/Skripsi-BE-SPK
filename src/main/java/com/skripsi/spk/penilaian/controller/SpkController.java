package com.skripsi.spk.penilaian.controller;

import com.skripsi.spk.penilaian.model.entity.HistoryRekomendasi;
import com.skripsi.spk.penilaian.repository.HasilRekomendasiRepository;
import com.skripsi.spk.penilaian.repository.HistoryRekomendasiRepository;
import com.skripsi.spk.penilaian.service.interfaces.SpkCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spk")
@RequiredArgsConstructor
public class SpkController {

    private final SpkCalculationService spkService;
    private final HistoryRekomendasiRepository historyRepository;
    private final HasilRekomendasiRepository hasilRepo;

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

    @GetMapping("/history/{nim}")
    public ResponseEntity<List<HistoryRekomendasi>> getHistoryMahasiswa(@PathVariable String nim) {
        return ResponseEntity.ok(historyRepository.findByNimOrderByTanggalPenilaianDesc(nim));
    }

    @GetMapping("/hasil-aktif/{nim}")
    public ResponseEntity<?> getHasilAktif(@PathVariable String nim) {
        try {
            var hasil = hasilRepo.findByNim(nim);
            return ResponseEntity.ok(hasil);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/download-pdf/{historyId}")
    public ResponseEntity<byte[]> downloadRaportPdf(@PathVariable Long historyId) {
        return spkService.generateRaportPdf(historyId);
    }

    @GetMapping("/download-pdf-by-nim/{nim}")
    public ResponseEntity<byte[]> downloadRaportPdfByNim(@PathVariable String nim) {
        try {
            // Cari history paling baru (teratas) milik mahasiswa berdasarkan NIM
            List<HistoryRekomendasi> logs = historyRepository.findByNimOrderByTanggalPenilaianDesc(nim);

            if (logs.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Ambil objek riwayat yang paling baru (indeks ke-0)
            Long latestHistoryId = logs.get(0).getId();

            // Alihkan ke fungsi generator PDF utama yang sudah kita buat sebelumnya
            return spkService.generateRaportPdf(latestHistoryId);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}