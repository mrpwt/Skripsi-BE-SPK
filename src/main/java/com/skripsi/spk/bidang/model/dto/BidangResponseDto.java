package com.skripsi.spk.bidang.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BidangResponseDto {
    private Long id;
    private String kode;
    private String namaBidang;
    private String deskripsi;
}
