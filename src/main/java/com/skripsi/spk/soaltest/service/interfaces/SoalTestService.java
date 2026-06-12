package com.skripsi.spk.soaltest.service.interfaces;

import com.skripsi.spk.soaltest.model.dto.SoalTestRequestDto;
import com.skripsi.spk.soaltest.model.dto.SoalTestResponseDto;
import com.skripsi.spk.soaltest.model.entity.SoalTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SoalTestService {

    void saveOrUpdate(SoalTestRequestDto dto);

    void delete(Integer id);

    List<SoalTestResponseDto> getAll();

    Page<SoalTestResponseDto> getAllAdmin(String keyword, Integer bidangId, int page, int size);

    SoalTestResponseDto getById(Integer id);

    Integer getJumlahSoalConfig();
    void saveJumlahSoalConfig(Integer jumlahSoal);
    List<SoalTestResponseDto> generateUjianSesuaiKebutuhan();
}
