package com.skripsi.spk.bidang.service.impl;

import com.skripsi.spk.bidang.model.dto.BidangRequestDto;
import com.skripsi.spk.bidang.model.dto.BidangResponseDto;
import com.skripsi.spk.bidang.model.entity.Bidang;
import com.skripsi.spk.bidang.repository.BidangRepository;
import com.skripsi.spk.bidang.service.interfaces.BidangService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidangServiceImpl implements BidangService {

    private final BidangRepository bidangRepository;

    public BidangServiceImpl(BidangRepository bidangRepository) {
        this.bidangRepository = bidangRepository;
    }

    @Override
    public List<BidangResponseDto> getAll() {
        return bidangRepository.findAll(Sort.by("createdAt").ascending())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BidangResponseDto getById(Long id) {
        Bidang bidang = bidangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bidang tidak ditemukan"));

        return toDto(bidang);
    }

    @Override
    public BidangResponseDto save(BidangRequestDto dto) {
        Bidang bidang;

        if (dto.getId() != null) {
            // UPDATE
            bidang = bidangRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Bidang tidak ditemukan"));
        } else {
            // CREATE
            if (bidangRepository.existsByKode(dto.getKode())) {
                throw new RuntimeException("Kode bidang sudah digunakan");
            }
            bidang = new Bidang();
        }

        bidang.setKode(dto.getKode());
        bidang.setNamaBidang(dto.getNamaBidang());
        bidang.setDeskripsi(dto.getDeskripsi());

        Bidang saved = bidangRepository.save(bidang);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        bidangRepository.deleteById(id);
    }

    /* =======================
       Mapping Entity -> DTO
       ======================= */
    private BidangResponseDto toDto(Bidang bidang) {
        BidangResponseDto dto = new BidangResponseDto();
        dto.setId(bidang.getId());
        dto.setKode(bidang.getKode());
        dto.setNamaBidang(bidang.getNamaBidang());
        dto.setDeskripsi(bidang.getDeskripsi());
        return dto;
    }
}
