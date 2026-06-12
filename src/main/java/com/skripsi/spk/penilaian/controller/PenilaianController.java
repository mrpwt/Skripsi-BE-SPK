package com.skripsi.spk.penilaian.controller;

import com.skripsi.spk.penilaian.model.dto.AnalitikResponse;
import com.skripsi.spk.penilaian.model.dto.PenilaianBulkRequest;
import com.skripsi.spk.penilaian.service.interfaces.PenilaianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/penilaian")
@RequiredArgsConstructor
public class PenilaianController {

    private final PenilaianService service;

    @PostMapping("/bulk")
    public ResponseEntity<?> saveOrUpdateBulk(
            @RequestBody PenilaianBulkRequest request) {

        service.saveOrUpdateBulk(request);

        return ResponseEntity.ok(Map.of(
                "message", "Penilaian mahasiswa berhasil disimpan / diperbarui"
        ));
    }

    @GetMapping("/analitik")
    public ResponseEntity<AnalitikResponse> getAnalitik() {
        return ResponseEntity.ok(service.getDashboardStats());
    }
}

