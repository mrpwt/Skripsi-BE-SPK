package com.skripsi.spk.penilaian.controller;

import com.skripsi.spk.penilaian.model.dto.PenilaianBulkRequest;
import com.skripsi.spk.penilaian.service.interfaces.PenilaianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

