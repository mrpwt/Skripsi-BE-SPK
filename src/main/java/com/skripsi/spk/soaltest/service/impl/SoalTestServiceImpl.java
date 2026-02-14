package com.skripsi.spk.soaltest.service.impl;

import com.skripsi.spk.soaltest.model.dto.SoalTestRequestDto;
import com.skripsi.spk.soaltest.model.dto.SoalTestResponseDto;
import com.skripsi.spk.soaltest.model.entity.SoalTest;
import com.skripsi.spk.soaltest.repository.SoalTestRepository;
import com.skripsi.spk.soaltest.service.interfaces.SoalTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoalTestServiceImpl implements SoalTestService {

    private final SoalTestRepository repository;

    @Override
    public void saveOrUpdate(SoalTestRequestDto dto) {
        SoalTest soal;

        if (dto.getId() != null) {
            soal = repository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Soal tidak ditemukan"));
        } else {
            soal = new SoalTest();
        }

        try {
            soal.setPertanyaan(dto.getPertanyaan());
            soal.setOpsi(dto.getOpsi());
            soal.setJawabanBenar(dto.getJawabanBenar());
            soal.setBidangId(dto.getBidangId());

            repository.save(soal);

        } catch (Exception e) {
            throw new RuntimeException("Error: ", e);
        }
    }

    @Override
    public void delete(Integer id) {
        SoalTest soal = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soal tidak ditemukan"));

        soal.setIsDeleted(true);
        repository.save(soal);
    }

    @Override
    public List<SoalTestResponseDto> getAll() {
        return repository.findByIsDeletedFalseOrderByCreatedAtAsc()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public SoalTestResponseDto getById(Integer id) {
        SoalTest soal = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Soal tidak ditemukan"));

        return toResponseDto(soal);
    }

    private SoalTestResponseDto toResponseDto(SoalTest soal) {
        return SoalTestResponseDto.builder()
                .id(soal.getId())
                .pertanyaan(soal.getPertanyaan())
                .opsi(soal.getOpsi())
                .jawabanBenar(soal.getJawabanBenar())
                .bidangId(soal.getBidangId())
                .build();
    }
}
