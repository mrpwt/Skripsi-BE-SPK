package com.skripsi.spk.soaltest.controller;

import com.skripsi.spk.soaltest.model.dto.SoalTestRequestDto;
import com.skripsi.spk.soaltest.model.dto.SoalTestResponseDto;
import com.skripsi.spk.soaltest.service.interfaces.SoalTestService;
import lombok.RequiredArgsConstructor;
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
}
