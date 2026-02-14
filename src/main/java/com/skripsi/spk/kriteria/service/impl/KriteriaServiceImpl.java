package com.skripsi.spk.kriteria.service.impl;

import com.skripsi.spk.kriteria.model.dto.KriteriaRequestDto;
import com.skripsi.spk.kriteria.model.dto.KriteriaResponseDto;
import com.skripsi.spk.kriteria.model.entity.Kriteria;
import com.skripsi.spk.kriteria.repository.KriteriaRepository;
import com.skripsi.spk.kriteria.service.interfaces.KriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KriteriaServiceImpl implements KriteriaService {

    private final KriteriaRepository repository;

    @Override
    public KriteriaResponseDto saveOrUpdate(KriteriaRequestDto request) {
        Kriteria kriteria = request.getId() != null
                ? repository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Kriteria tidak ditemukan"))
                : new Kriteria();

        kriteria.setKode(request.getKode());
        kriteria.setNamaKriteria(request.getNamaKriteria());
        kriteria.setBobot(request.getBobot());
        kriteria.setTipe(request.getTipe());

        return toDto(repository.save(kriteria));
    }

    @Override
    public List<KriteriaResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Kriteria::getCreatedAt))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public KriteriaResponseDto getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Kriteria tidak ditemukan"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private KriteriaResponseDto toDto(Kriteria kriteria) {
        KriteriaResponseDto dto = new KriteriaResponseDto();
        dto.setId(kriteria.getId());
        dto.setKode(kriteria.getKode());
        dto.setNamaKriteria(kriteria.getNamaKriteria());
        dto.setBobot(kriteria.getBobot());
        dto.setTipe(kriteria.getTipe());
        return dto;
    }
}
