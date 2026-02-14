package com.skripsi.spk.soaltest.service.interfaces;

import com.skripsi.spk.soaltest.model.dto.SoalTestRequestDto;
import com.skripsi.spk.soaltest.model.dto.SoalTestResponseDto;

import java.util.List;

public interface SoalTestService {

    void saveOrUpdate(SoalTestRequestDto dto);

    void delete(Integer id);

    List<SoalTestResponseDto> getAll();

    SoalTestResponseDto getById(Integer id);
}
