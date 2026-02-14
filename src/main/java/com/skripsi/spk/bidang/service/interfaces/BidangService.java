package com.skripsi.spk.bidang.service.interfaces;

import com.skripsi.spk.bidang.model.dto.BidangRequestDto;
import com.skripsi.spk.bidang.model.dto.BidangResponseDto;

import java.util.List;

public interface BidangService {

    List<BidangResponseDto> getAll();

    BidangResponseDto getById(Long id);

    BidangResponseDto save(BidangRequestDto dto);

    void delete(Long id);
}
