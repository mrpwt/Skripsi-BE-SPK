package com.skripsi.spk.soaltest.controller;

import com.skripsi.spk.soaltest.model.dto.SoalTestRequestDto;
import com.skripsi.spk.soaltest.model.dto.SoalTestResponseDto;
import com.skripsi.spk.soaltest.service.interfaces.SoalTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soal-tes")
@RequiredArgsConstructor
public class SoalTestController {

    private final SoalTestService soalTesService;

    /**
     * CREATE & UPDATE
     */
    @PostMapping
    public ResponseEntity<?> saveOrUpdate(@RequestBody SoalTestRequestDto dto) {
        soalTesService.saveOrUpdate(dto);
        return ResponseEntity.ok("Berhasil disimpan");
    }

    /**
     * GET ALL (is_deleted = false)
     */
    @GetMapping
    public ResponseEntity<List<SoalTestResponseDto>> getAll() {
        return ResponseEntity.ok(soalTesService.getAll());
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<SoalTestResponseDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer bidangId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(soalTesService.getAllAdmin(keyword, bidangId, page, size));
    }

    /**
     * GET BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SoalTestResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(soalTesService.getById(id));
    }

    /**
     * DELETE (Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        soalTesService.delete(id);
        return ResponseEntity.ok("Berhasil dihapus");
    }

    @GetMapping("/config")
    public ResponseEntity<Integer> getConfig() {
        return ResponseEntity.ok(soalTesService.getJumlahSoalConfig());
    }

    @PostMapping("/config")
    public ResponseEntity<?> updateConfig(@RequestParam Integer jumlahSoal) {
        try {
            soalTesService.saveJumlahSoalConfig(jumlahSoal);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint yang dipanggil oleh halaman user/peserta saat mulai tes
    @GetMapping("/generate")
    public ResponseEntity<List<SoalTestResponseDto>> getSoalUjianUser() {
        return ResponseEntity.ok(soalTesService.generateUjianSesuaiKebutuhan());
    }
}
