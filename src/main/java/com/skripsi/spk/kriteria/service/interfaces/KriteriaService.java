package com.skripsi.spk.kriteria.service.interfaces;

import com.skripsi.spk.kriteria.model.dto.KriteriaRequestDto;
import com.skripsi.spk.kriteria.model.dto.KriteriaResponseDto;

import java.util.List;

public interface KriteriaService {

    KriteriaResponseDto saveOrUpdate(KriteriaRequestDto request);

    List<KriteriaResponseDto> getAll();

    KriteriaResponseDto getById(Long id);

    void delete(Long id);
}
