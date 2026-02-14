package com.skripsi.spk.kriteria.controller;

import com.skripsi.spk.kriteria.model.dto.KriteriaRequestDto;
import com.skripsi.spk.kriteria.model.dto.KriteriaResponseDto;
import com.skripsi.spk.kriteria.service.interfaces.KriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kriteria")
@RequiredArgsConstructor
public class KriteriaController {

    private final KriteriaService service;

    @PostMapping
    public KriteriaResponseDto createOrUpdate(@RequestBody KriteriaRequestDto request) {
        return service.saveOrUpdate(request);
    }

    @GetMapping
    public List<KriteriaResponseDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public KriteriaResponseDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
